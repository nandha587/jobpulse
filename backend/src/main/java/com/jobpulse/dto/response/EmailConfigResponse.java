package com.jobpulse.dto.response;

import java.time.Instant;

public class EmailConfigResponse {
    private boolean configured;
    private String emailAddress;
    private String imapHost;
    private int imapPort;
    private boolean autoSyncEnabled;
    private Instant lastSyncedAt;
    private String lastSyncStatus;
    private String lastSyncMessage;

    public EmailConfigResponse() {}

    public EmailConfigResponse(boolean configured, String emailAddress, String imapHost, int imapPort,
                               boolean autoSyncEnabled, Instant lastSyncedAt, String lastSyncStatus, String lastSyncMessage) {
        this.configured = configured;
        this.emailAddress = emailAddress;
        this.imapHost = imapHost;
        this.imapPort = imapPort;
        this.autoSyncEnabled = autoSyncEnabled;
        this.lastSyncedAt = lastSyncedAt;
        this.lastSyncStatus = lastSyncStatus;
        this.lastSyncMessage = lastSyncMessage;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private boolean configured;
        private String emailAddress;
        private String imapHost;
        private int imapPort;
        private boolean autoSyncEnabled;
        private Instant lastSyncedAt;
        private String lastSyncStatus;
        private String lastSyncMessage;

        public Builder configured(boolean configured) { this.configured = configured; return this; }
        public Builder emailAddress(String emailAddress) { this.emailAddress = emailAddress; return this; }
        public Builder imapHost(String imapHost) { this.imapHost = imapHost; return this; }
        public Builder imapPort(int imapPort) { this.imapPort = imapPort; return this; }
        public Builder autoSyncEnabled(boolean autoSyncEnabled) { this.autoSyncEnabled = autoSyncEnabled; return this; }
        public Builder lastSyncedAt(Instant lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; return this; }
        public Builder lastSyncStatus(String lastSyncStatus) { this.lastSyncStatus = lastSyncStatus; return this; }
        public Builder lastSyncMessage(String lastSyncMessage) { this.lastSyncMessage = lastSyncMessage; return this; }

        public EmailConfigResponse build() {
            return new EmailConfigResponse(configured, emailAddress, imapHost, imapPort, autoSyncEnabled, lastSyncedAt, lastSyncStatus, lastSyncMessage);
        }
    }

    public boolean isConfigured() { return configured; }
    public void setConfigured(boolean configured) { this.configured = configured; }

    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getImapHost() { return imapHost; }
    public void setImapHost(String imapHost) { this.imapHost = imapHost; }

    public int getImapPort() { return imapPort; }
    public void setImapPort(int imapPort) { this.imapPort = imapPort; }

    public boolean isAutoSyncEnabled() { return autoSyncEnabled; }
    public void setAutoSyncEnabled(boolean autoSyncEnabled) { this.autoSyncEnabled = autoSyncEnabled; }

    public Instant getLastSyncedAt() { return lastSyncedAt; }
    public void setLastSyncedAt(Instant lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; }

    public String getLastSyncStatus() { return lastSyncStatus; }
    public void setLastSyncStatus(String lastSyncStatus) { this.lastSyncStatus = lastSyncStatus; }

    public String getLastSyncMessage() { return lastSyncMessage; }
    public void setLastSyncMessage(String lastSyncMessage) { this.lastSyncMessage = lastSyncMessage; }
}
