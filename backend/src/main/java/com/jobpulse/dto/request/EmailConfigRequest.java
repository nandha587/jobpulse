package com.jobpulse.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailConfigRequest {

    @NotBlank(message = "Email address is required")
    @Email(message = "Invalid email format")
    private String emailAddress;

    @NotBlank(message = "IMAP host is required")
    private String imapHost = "imap.gmail.com";

    private int imapPort = 993;

    @NotBlank(message = "App password is required")
    private String appPassword;

    private boolean autoSyncEnabled = true;

    public EmailConfigRequest() {}

    public EmailConfigRequest(String emailAddress, String imapHost, int imapPort, String appPassword, boolean autoSyncEnabled) {
        this.emailAddress = emailAddress;
        this.imapHost = imapHost;
        this.imapPort = imapPort;
        this.appPassword = appPassword;
        this.autoSyncEnabled = autoSyncEnabled;
    }

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
}
