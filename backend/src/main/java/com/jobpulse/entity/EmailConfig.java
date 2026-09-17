package com.jobpulse.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "email_configs")
public class EmailConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "imap_host", nullable = false)
    private String imapHost = "imap.gmail.com";

    @Column(name = "imap_port", nullable = false)
    private int imapPort = 993;

    @Column(name = "app_password", nullable = false)
    private String appPassword;

    @Column(name = "auto_sync_enabled", nullable = false)
    private boolean autoSyncEnabled = true;

    @Column(name = "last_synced_at")
    private Instant lastSyncedAt;

    @Column(name = "last_sync_status")
    private String lastSyncStatus;

    @Column(name = "last_sync_message", length = 500)
    private String lastSyncMessage;

    public EmailConfig() {}

    public EmailConfig(Long id, User user, String emailAddress, String imapHost, int imapPort,
                       String appPassword, boolean autoSyncEnabled, Instant lastSyncedAt,
                       String lastSyncStatus, String lastSyncMessage) {
        this.id = id;
        this.user = user;
        this.emailAddress = emailAddress;
        this.imapHost = imapHost;
        this.imapPort = imapPort;
        this.appPassword = appPassword;
        this.autoSyncEnabled = autoSyncEnabled;
        this.lastSyncedAt = lastSyncedAt;
        this.lastSyncStatus = lastSyncStatus;
        this.lastSyncMessage = lastSyncMessage;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private String emailAddress;
        private String imapHost = "imap.gmail.com";
        private int imapPort = 993;
        private String appPassword;
        private boolean autoSyncEnabled = true;
        private Instant lastSyncedAt;
        private String lastSyncStatus;
        private String lastSyncMessage;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder emailAddress(String emailAddress) { this.emailAddress = emailAddress; return this; }
        public Builder imapHost(String imapHost) { this.imapHost = imapHost; return this; }
        public Builder imapPort(int imapPort) { this.imapPort = imapPort; return this; }
        public Builder appPassword(String appPassword) { this.appPassword = appPassword; return this; }
        public Builder autoSyncEnabled(boolean autoSyncEnabled) { this.autoSyncEnabled = autoSyncEnabled; return this; }
        public Builder lastSyncedAt(Instant lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; return this; }
        public Builder lastSyncStatus(String lastSyncStatus) { this.lastSyncStatus = lastSyncStatus; return this; }
        public Builder lastSyncMessage(String lastSyncMessage) { this.lastSyncMessage = lastSyncMessage; return this; }

        public EmailConfig build() {
            return new EmailConfig(id, user, emailAddress, imapHost, imapPort, appPassword, autoSyncEnabled, lastSyncedAt, lastSyncStatus, lastSyncMessage);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getImapHost() { return imapHost; }
    public void setImapHost(String imapHost) { this.imapHost = imapHost; }

    public int getImapPort() { return imapPort; }
    public void setImapPort(int imapPort) { this.imapPort = imapPort; }

    public String getAppPassword() { return appPassword; }
    public void setAppPassword(String appPassword) { this.appPassword = appPassword; }

    public boolean isAutoSyncEnabled() { return autoSyncEnabled; }
    public void setAutoSyncEnabled(boolean autoSyncEnabled) { this.autoSyncEnabled = autoSyncEnabled; }

    public Instant getLastSyncedAt() { return lastSyncedAt; }
    public void setLastSyncedAt(Instant lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; }

    public String getLastSyncStatus() { return lastSyncStatus; }
    public void setLastSyncStatus(String lastSyncStatus) { this.lastSyncStatus = lastSyncStatus; }

    public String getLastSyncMessage() { return lastSyncMessage; }
    public void setLastSyncMessage(String lastSyncMessage) { this.lastSyncMessage = lastSyncMessage; }
}
