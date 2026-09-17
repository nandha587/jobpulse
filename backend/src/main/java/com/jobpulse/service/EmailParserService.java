package com.jobpulse.service;

import com.jobpulse.dto.ParsedApplicationEmail;
import com.jobpulse.entity.enums.ApplicationStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailParserService {

    // LinkedIn Patterns
    // "You applied to [Job Title] at [Company]"
    private static final Pattern LINKEDIN_APPLIED_1 = Pattern.compile("You applied to (.+?) at (.+?)(?:\\.|$)", Pattern.CASE_INSENSITIVE);
    // "Your application for [Job Title] was submitted to [Company]"
    private static final Pattern LINKEDIN_APPLIED_2 = Pattern.compile("Your application for (.+?) was submitted to (.+?)(?:\\.|$)", Pattern.CASE_INSENSITIVE);
    // "Your application to [Job Title] at [Company]"
    private static final Pattern LINKEDIN_APPLIED_3 = Pattern.compile("Your application to (.+?) at (.+?)(?:\\.|$)", Pattern.CASE_INSENSITIVE);
    // "Application sent to [Company] for [Job Title]"
    private static final Pattern LINKEDIN_APPLIED_4 = Pattern.compile("Application sent to (.+?) for (.+?)(?:\\.|$)", Pattern.CASE_INSENSITIVE);
    // Interview from LinkedIn: "Invitation to interview from [Company] for [Job Title]"
    private static final Pattern LINKEDIN_INTERVIEW = Pattern.compile("(?:Interview|Invitation).+?(?:at|from|with) (.+?) for (.+?)(?:\\.|$)", Pattern.CASE_INSENSITIVE);

    // Naukri Patterns
    // "Application sent for [Job Title] at [Company]"
    private static final Pattern NAUKRI_APPLIED_1 = Pattern.compile("Application sent for (.+?) (?:at|in) (.+?)(?:\\s*[-|,]|$)", Pattern.CASE_INSENSITIVE);
    // "Your application for [Job Title] has been sent to [Company]"
    private static final Pattern NAUKRI_APPLIED_2 = Pattern.compile("Your application for (.+?) has been sent to (.+?)(?:\\s*[-|,]|$)", Pattern.CASE_INSENSITIVE);
    // "Application for [Job Title] sent to [Company]"
    private static final Pattern NAUKRI_APPLIED_3 = Pattern.compile("Application for (.+?) sent to (.+?)(?:\\s*[-|,]|$)", Pattern.CASE_INSENSITIVE);
    // "Recruiter from [Company] viewed your application for [Job Title]"
    private static final Pattern NAUKRI_VIEWED = Pattern.compile("Recruiter (?:from|at) (.+?) viewed your application for (.+?)(?:\\s*[-|,]|$)", Pattern.CASE_INSENSITIVE);
    // "Interview invitation from [Company] for [Job Title]"
    private static final Pattern NAUKRI_INTERVIEW = Pattern.compile("Interview (?:invitation|call) from (.+?) for (.+?)(?:\\s*[-|,]|$)", Pattern.CASE_INSENSITIVE);

    // Generic / Indeed / ATS Patterns
    // "Indeed Application: [Job Title] at [Company]"
    private static final Pattern INDEED_APPLIED = Pattern.compile("Indeed Application:\\s*(.+?) at (.+?)(?:\\s*[-|,]|$)", Pattern.CASE_INSENSITIVE);
    // "Thank you for applying to [Company] for [Job Title]"
    private static final Pattern GENERIC_APPLIED_1 = Pattern.compile("Thank you for applying to (.+?) for (?:the position of )?(.+?)(?:\\s*[-|,]|$)", Pattern.CASE_INSENSITIVE);
    // "Application received: [Job Title] at [Company]"
    private static final Pattern GENERIC_APPLIED_2 = Pattern.compile("Application received:\\s*(.+?) at (.+?)(?:\\s*[-|,]|$)", Pattern.CASE_INSENSITIVE);

    public ParsedApplicationEmail parse(String sender, String subject, String body) {
        if (subject == null) subject = "";
        if (sender == null) sender = "";
        if (body == null) body = "";

        String senderLower = sender.toLowerCase();
        String textToAnalyze = subject.trim();

        // 1. LinkedIn
        if (senderLower.contains("linkedin.com") || textToAnalyze.toLowerCase().contains("linkedin")) {
            ParsedApplicationEmail result = parseLinkedIn(textToAnalyze, body);
            if (result != null && result.isValid()) return result;
        }

        // 2. Naukri
        if (senderLower.contains("naukri.com") || textToAnalyze.toLowerCase().contains("naukri")) {
            ParsedApplicationEmail result = parseNaukri(textToAnalyze, body);
            if (result != null && result.isValid()) return result;
        }

        // 3. Indeed
        if (senderLower.contains("indeed.com") || textToAnalyze.toLowerCase().contains("indeed")) {
            ParsedApplicationEmail result = parseIndeed(textToAnalyze, body);
            if (result != null && result.isValid()) return result;
        }

        // 4. Generic ATS (Greenhouse, Lever, Workday, etc.)
        ParsedApplicationEmail genericResult = parseGeneric(textToAnalyze, body, senderLower);
        if (genericResult != null && genericResult.isValid()) {
            return genericResult;
        }

        // Fallback: search within body if subject didn't match
        ParsedApplicationEmail bodyResult = parseBodyFallback(body, senderLower);
        if (bodyResult != null && bodyResult.isValid()) {
            return bodyResult;
        }

        return ParsedApplicationEmail.builder().valid(false).build();
    }

    private ParsedApplicationEmail parseLinkedIn(String subject, String body) {
        Matcher m = LINKEDIN_INTERVIEW.matcher(subject);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .companyName(clean(m.group(1)))
                    .jobTitle(clean(m.group(2)))
                    .source("LINKEDIN")
                    .status(ApplicationStatus.INTERVIEW)
                    .statusUpdate(true)
                    .notes("Interview notification received via LinkedIn email.")
                    .build();
        }

        m = LINKEDIN_APPLIED_1.matcher(subject);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .jobTitle(clean(m.group(1)))
                    .companyName(clean(m.group(2)))
                    .source("LINKEDIN")
                    .status(ApplicationStatus.APPLIED)
                    .notes("Auto-imported from LinkedIn confirmation email.")
                    .build();
        }

        m = LINKEDIN_APPLIED_2.matcher(subject);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .jobTitle(clean(m.group(1)))
                    .companyName(clean(m.group(2)))
                    .source("LINKEDIN")
                    .status(ApplicationStatus.APPLIED)
                    .notes("Auto-imported from LinkedIn confirmation email.")
                    .build();
        }

        m = LINKEDIN_APPLIED_3.matcher(subject);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .jobTitle(clean(m.group(1)))
                    .companyName(clean(m.group(2)))
                    .source("LINKEDIN")
                    .status(ApplicationStatus.APPLIED)
                    .notes("Auto-imported from LinkedIn confirmation email.")
                    .build();
        }

        m = LINKEDIN_APPLIED_4.matcher(subject);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .companyName(clean(m.group(1)))
                    .jobTitle(clean(m.group(2)))
                    .source("LINKEDIN")
                    .status(ApplicationStatus.APPLIED)
                    .notes("Auto-imported from LinkedIn confirmation email.")
                    .build();
        }

        return null;
    }

    private ParsedApplicationEmail parseNaukri(String subject, String body) {
        Matcher m = NAUKRI_INTERVIEW.matcher(subject);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .companyName(clean(m.group(1)))
                    .jobTitle(clean(m.group(2)))
                    .source("NAUKRI")
                    .status(ApplicationStatus.INTERVIEW)
                    .statusUpdate(true)
                    .notes("Interview invitation received via Naukri.")
                    .build();
        }

        m = NAUKRI_VIEWED.matcher(subject);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .companyName(clean(m.group(1)))
                    .jobTitle(clean(m.group(2)))
                    .source("NAUKRI")
                    .status(ApplicationStatus.APPLIED)
                    .statusUpdate(true)
                    .notes("Recruiter viewed application on Naukri.")
                    .build();
        }

        m = NAUKRI_APPLIED_1.matcher(subject);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .jobTitle(clean(m.group(1)))
                    .companyName(clean(m.group(2)))
                    .source("NAUKRI")
                    .status(ApplicationStatus.APPLIED)
                    .notes("Auto-imported from Naukri application confirmation.")
                    .build();
        }

        m = NAUKRI_APPLIED_2.matcher(subject);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .jobTitle(clean(m.group(1)))
                    .companyName(clean(m.group(2)))
                    .source("NAUKRI")
                    .status(ApplicationStatus.APPLIED)
                    .notes("Auto-imported from Naukri application confirmation.")
                    .build();
        }

        m = NAUKRI_APPLIED_3.matcher(subject);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .jobTitle(clean(m.group(1)))
                    .companyName(clean(m.group(2)))
                    .source("NAUKRI")
                    .status(ApplicationStatus.APPLIED)
                    .notes("Auto-imported from Naukri application confirmation.")
                    .build();
        }

        return null;
    }

    private ParsedApplicationEmail parseIndeed(String subject, String body) {
        Matcher m = INDEED_APPLIED.matcher(subject);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .jobTitle(clean(m.group(1)))
                    .companyName(clean(m.group(2)))
                    .source("INDEED")
                    .status(ApplicationStatus.APPLIED)
                    .notes("Auto-imported from Indeed confirmation email.")
                    .build();
        }
        return null;
    }

    private ParsedApplicationEmail parseGeneric(String subject, String body, String sender) {
        Matcher m = GENERIC_APPLIED_1.matcher(subject);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .companyName(clean(m.group(1)))
                    .jobTitle(clean(m.group(2)))
                    .source(detectSource(sender))
                    .status(ApplicationStatus.APPLIED)
                    .notes("Auto-imported from employer confirmation email.")
                    .build();
        }

        m = GENERIC_APPLIED_2.matcher(subject);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .jobTitle(clean(m.group(1)))
                    .companyName(clean(m.group(2)))
                    .source(detectSource(sender))
                    .status(ApplicationStatus.APPLIED)
                    .notes("Auto-imported from employer confirmation email.")
                    .build();
        }

        return null;
    }

    private ParsedApplicationEmail parseBodyFallback(String body, String sender) {
        if (body == null || body.isBlank()) return null;

        // Try extracting Company: X, Role: Y or Position: Y from body
        Pattern bodyPattern = Pattern.compile("(?:Position|Role|Job Title)\\s*:\\s*([^\r\n]+)[\\r\\n]+(?:Company|Employer|Organization)\\s*:\\s*([^\r\n]+)", Pattern.CASE_INSENSITIVE);
        Matcher m = bodyPattern.matcher(body);
        if (m.find()) {
            return ParsedApplicationEmail.builder()
                    .jobTitle(clean(m.group(1)))
                    .companyName(clean(m.group(2)))
                    .source(detectSource(sender))
                    .status(ApplicationStatus.APPLIED)
                    .notes("Auto-imported from email body content.")
                    .build();
        }
        return null;
    }

    private String detectSource(String sender) {
        if (sender.contains("linkedin")) return "LINKEDIN";
        if (sender.contains("naukri")) return "NAUKRI";
        if (sender.contains("indeed")) return "INDEED";
        if (sender.contains("greenhouse")) return "GREENHOUSE";
        if (sender.contains("lever")) return "LEVER";
        if (sender.contains("workday")) return "WORKDAY";
        return "EMAIL_SYNC";
    }

    private String clean(String text) {
        if (text == null) return "";
        return text.replaceAll("[\"'\\[\\]]", "")
                   .replaceAll("\\s+", " ")
                   .replaceAll("^(at|for|to|in)\\s+", "")
                   .trim();
    }
}
