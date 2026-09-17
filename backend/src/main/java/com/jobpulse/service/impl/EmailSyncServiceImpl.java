package com.jobpulse.service.impl;

import com.jobpulse.dto.ParsedApplicationEmail;
import com.jobpulse.dto.request.EmailConfigRequest;
import com.jobpulse.dto.request.EmailSimulateRequest;
import com.jobpulse.dto.response.EmailConfigResponse;
import com.jobpulse.dto.response.EmailSyncResponse;
import com.jobpulse.entity.Application;
import com.jobpulse.entity.ApplicationStatusHistory;
import com.jobpulse.entity.EmailConfig;
import com.jobpulse.entity.User;
import com.jobpulse.entity.enums.ApplicationStatus;
import com.jobpulse.entity.enums.JobType;
import com.jobpulse.entity.enums.Priority;
import com.jobpulse.exception.BadRequestException;
import com.jobpulse.exception.ResourceNotFoundException;
import com.jobpulse.repository.ApplicationRepository;
import com.jobpulse.repository.ApplicationStatusHistoryRepository;
import com.jobpulse.repository.EmailConfigRepository;
import com.jobpulse.repository.UserRepository;
import com.jobpulse.service.EmailParserService;
import com.jobpulse.service.EmailSyncService;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.FromStringTerm;
import jakarta.mail.search.OrTerm;
import jakarta.mail.search.SearchTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
public class EmailSyncServiceImpl implements EmailSyncService {

    private static final Logger log = LoggerFactory.getLogger(EmailSyncServiceImpl.class);

    private final EmailConfigRepository emailConfigRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationStatusHistoryRepository statusHistoryRepository;
    private final EmailParserService emailParserService;

    public EmailSyncServiceImpl(EmailConfigRepository emailConfigRepository,
                                UserRepository userRepository,
                                ApplicationRepository applicationRepository,
                                ApplicationStatusHistoryRepository statusHistoryRepository,
                                EmailParserService emailParserService) {
        this.emailConfigRepository = emailConfigRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.emailParserService = emailParserService;
    }

    @Override
    @Transactional(readOnly = true)
    public EmailConfigResponse getConfig(Long userId) {
        return emailConfigRepository.findByUserId(userId)
                .map(cfg -> EmailConfigResponse.builder()
                        .configured(true)
                        .emailAddress(cfg.getEmailAddress())
                        .imapHost(cfg.getImapHost())
                        .imapPort(cfg.getImapPort())
                        .autoSyncEnabled(cfg.isAutoSyncEnabled())
                        .lastSyncedAt(cfg.getLastSyncedAt())
                        .lastSyncStatus(cfg.getLastSyncStatus())
                        .lastSyncMessage(cfg.getLastSyncMessage())
                        .build())
                .orElse(EmailConfigResponse.builder()
                        .configured(false)
                        .imapHost("imap.gmail.com")
                        .imapPort(993)
                        .autoSyncEnabled(true)
                        .build());
    }

    @Override
    @Transactional
    public EmailConfigResponse saveConfig(Long userId, EmailConfigRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        EmailConfig config = emailConfigRepository.findByUserId(userId)
                .orElse(EmailConfig.builder().user(user).build());

        config.setEmailAddress(request.getEmailAddress().trim());
        config.setImapHost(request.getImapHost().trim());
        config.setImapPort(request.getImapPort());
        config.setAppPassword(request.getAppPassword().trim().replaceAll("\\s+", ""));
        config.setAutoSyncEnabled(request.isAutoSyncEnabled());

        EmailConfig saved = emailConfigRepository.save(config);

        return EmailConfigResponse.builder()
                .configured(true)
                .emailAddress(saved.getEmailAddress())
                .imapHost(saved.getImapHost())
                .imapPort(saved.getImapPort())
                .autoSyncEnabled(saved.isAutoSyncEnabled())
                .lastSyncedAt(saved.getLastSyncedAt())
                .lastSyncStatus(saved.getLastSyncStatus())
                .lastSyncMessage(saved.getLastSyncMessage())
                .build();
    }

    @Override
    public boolean testConnection(Long userId) {
        EmailConfig config = emailConfigRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("No email configuration found. Please save your email settings first."));

        try {
            Session session = createImapSession();
            Store store = session.getStore("imaps");
            String password = config.getAppPassword() != null ? config.getAppPassword().replaceAll("\\s+", "") : "";
            store.connect(config.getImapHost(), config.getImapPort(), config.getEmailAddress(), password);
            store.close();
            return true;
        } catch (Exception e) {
            log.error("Failed to connect to IMAP server for user {}: {}", userId, e.getMessage());
            throw new BadRequestException("Connection failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public EmailSyncResponse syncUserEmails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        EmailConfig config = emailConfigRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("Email sync is not configured. Please save your email settings."));

        int scanned = 0;
        int created = 0;
        int updated = 0;
        List<String> details = new ArrayList<>();

        Store store = null;
        Folder folder = null;
        try {
            Session session = createImapSession();
            store = session.getStore("imaps");
            String password = config.getAppPassword() != null ? config.getAppPassword().replaceAll("\\s+", "") : "";
            store.connect(config.getImapHost(), config.getImapPort(), config.getEmailAddress(), password);

            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            // Search for messages from common job boards
            SearchTerm term = new OrTerm(new SearchTerm[]{
                    new FromStringTerm("linkedin.com"),
                    new FromStringTerm("naukri.com"),
                    new FromStringTerm("indeed.com"),
                    new FromStringTerm("greenhouse.io"),
                    new FromStringTerm("lever.co")
            });

            Message[] messages = folder.search(term);
            scanned = messages.length;

            // Process most recent up to 50 messages to avoid timeouts
            int startIndex = Math.max(0, messages.length - 50);
            for (int i = messages.length - 1; i >= startIndex; i--) {
                Message msg = messages[i];
                try {
                    String sender = getFromAddress(msg);
                    String subject = msg.getSubject() != null ? msg.getSubject() : "";
                    String body = extractBody(msg);

                    ParsedApplicationEmail parsed = emailParserService.parse(sender, subject, body);
                    if (parsed != null && parsed.isValid() && parsed.getCompanyName() != null && parsed.getJobTitle() != null) {
                        SyncResult res = processParsedEmail(user, parsed);
                        if (res.created) {
                            created++;
                            details.add("Created: " + parsed.getJobTitle() + " at " + parsed.getCompanyName() + " (" + parsed.getSource() + ")");
                        } else if (res.updated) {
                            updated++;
                            details.add("Updated status to " + parsed.getStatus() + ": " + parsed.getJobTitle() + " at " + parsed.getCompanyName());
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error processing individual message: {}", e.getMessage());
                }
            }

            config.setLastSyncedAt(Instant.now());
            config.setLastSyncStatus("SUCCESS");
            config.setLastSyncMessage(String.format("Synced successfully. Found %d messages, created %d, updated %d.", scanned, created, updated));
            emailConfigRepository.save(config);

            return EmailSyncResponse.builder()
                    .status("SUCCESS")
                    .message(String.format("Scan completed. Processed %d messages.", scanned))
                    .emailsScanned(scanned)
                    .applicationsCreated(created)
                    .applicationsUpdated(updated)
                    .details(details)
                    .build();

        } catch (Exception e) {
            log.error("Email sync error for user {}: {}", userId, e.getMessage());
            config.setLastSyncedAt(Instant.now());
            config.setLastSyncStatus("FAILED");
            config.setLastSyncMessage("Sync error: " + e.getMessage());
            emailConfigRepository.save(config);

            return EmailSyncResponse.builder()
                    .status("ERROR")
                    .message("Failed to sync emails: " + e.getMessage())
                    .emailsScanned(scanned)
                    .applicationsCreated(created)
                    .applicationsUpdated(updated)
                    .details(details)
                    .build();
        } finally {
            closeFolderAndStore(folder, store);
        }
    }

    @Override
    @Transactional
    public EmailSyncResponse simulateEmailIngestion(Long userId, EmailSimulateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        ParsedApplicationEmail parsed = emailParserService.parse(request.getSender(), request.getSubject(), request.getBody());
        if (parsed == null || !parsed.isValid() || parsed.getCompanyName() == null || parsed.getJobTitle() == null) {
            return EmailSyncResponse.builder()
                    .status("WARNING")
                    .message("The email was analyzed, but no recognized job application pattern was found in the subject or body.")
                    .emailsScanned(1)
                    .applicationsCreated(0)
                    .applicationsUpdated(0)
                    .details(List.of("Pattern not matched for subject: " + request.getSubject()))
                    .build();
        }

        SyncResult res = processParsedEmail(user, parsed);
        List<String> details = new ArrayList<>();
        int created = res.created ? 1 : 0;
        int updated = res.updated ? 1 : 0;

        if (res.created) {
            details.add("Created application for " + parsed.getJobTitle() + " at " + parsed.getCompanyName() + " (" + parsed.getSource() + ")");
        } else if (res.updated) {
            details.add("Updated status to " + parsed.getStatus() + " for " + parsed.getJobTitle() + " at " + parsed.getCompanyName());
        } else {
            details.add("Application already exists with latest status: " + parsed.getJobTitle() + " at " + parsed.getCompanyName());
        }

        return EmailSyncResponse.builder()
                .status("SUCCESS")
                .message("Successfully processed email.")
                .emailsScanned(1)
                .applicationsCreated(created)
                .applicationsUpdated(updated)
                .details(details)
                .build();
    }

    @Scheduled(fixedDelay = 900000) // every 15 minutes
    @Override
    public void runScheduledSync() {
        log.info("Starting background scheduled email sync for active accounts...");
        List<EmailConfig> activeConfigs = emailConfigRepository.findByAutoSyncEnabledTrue();
        for (EmailConfig cfg : activeConfigs) {
            try {
                syncUserEmails(cfg.getUser().getId());
            } catch (Exception e) {
                log.error("Scheduled sync error for user {}: {}", cfg.getUser().getId(), e.getMessage());
            }
        }
    }

    private SyncResult processParsedEmail(User user, ParsedApplicationEmail parsed) {
        Optional<Application> existing = applicationRepository
                .findByUserIdAndCompanyNameIgnoreCaseAndJobTitleIgnoreCase(user.getId(), parsed.getCompanyName(), parsed.getJobTitle());

        if (existing.isPresent()) {
            Application app = existing.get();
            if (parsed.isStatusUpdate() && app.getStatus() != parsed.getStatus()) {
                ApplicationStatus oldStatus = app.getStatus();
                app.setStatus(parsed.getStatus());
                applicationRepository.save(app);

                ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                        .application(app)
                        .previousStatus(oldStatus)
                        .newStatus(parsed.getStatus())
                        .build();
                statusHistoryRepository.save(history);
                return new SyncResult(false, true);
            }
            return new SyncResult(false, false);
        }

        Application newApp = Application.builder()
                .user(user)
                .companyName(parsed.getCompanyName())
                .jobTitle(parsed.getJobTitle())
                .location(parsed.getLocation() != null ? parsed.getLocation() : "Not Specified")
                .jobType(JobType.FULL_TIME)
                .applicationDate(parsed.getApplicationDate() != null ? parsed.getApplicationDate() : LocalDate.now())
                .source(parsed.getSource())
                .status(parsed.getStatus())
                .priority(Priority.MEDIUM)
                .salaryInfo(parsed.getSalaryInfo())
                .jobUrl(parsed.getJobUrl())
                .notesSummary(parsed.getNotes())
                .build();

        Application saved = applicationRepository.save(newApp);

        ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                .application(saved)
                .previousStatus(null)
                .newStatus(saved.getStatus())
                .build();
        statusHistoryRepository.save(history);

        return new SyncResult(true, false);
    }

    private Session createImapSession() {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.ssl.enable", "true");
        props.put("mail.imaps.ssl.trust", "*");
        props.put("mail.imaps.timeout", "10000");
        props.put("mail.imaps.connectiontimeout", "10000");
        return Session.getInstance(props);
    }

    private String getFromAddress(Message message) throws MessagingException {
        Address[] froms = message.getFrom();
        if (froms != null && froms.length > 0) {
            return froms[0].toString();
        }
        return "";
    }

    private String extractBody(Message message) {
        try {
            Object content = message.getContent();
            if (content instanceof String) {
                return (String) content;
            } else if (content instanceof MimeMultipart) {
                return extractFromMultipart((MimeMultipart) content);
            }
        } catch (Exception e) {
            log.warn("Could not extract message body: {}", e.getMessage());
        }
        return "";
    }

    private String extractFromMultipart(MimeMultipart multipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = multipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.isMimeType("text/html") && result.length() == 0) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(extractFromMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }

    private void closeFolderAndStore(Folder folder, Store store) {
        try {
            if (folder != null && folder.isOpen()) {
                folder.close(false);
            }
        } catch (Exception ignored) {}
        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (Exception ignored) {}
    }

    private static class SyncResult {
        final boolean created;
        final boolean updated;
        SyncResult(boolean created, boolean updated) {
            this.created = created;
            this.updated = updated;
        }
    }
}
