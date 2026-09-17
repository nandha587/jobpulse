package com.jobpulse.dto.response;

import java.util.ArrayList;
import java.util.List;

public class EmailSyncResponse {
    private String status;
    private String message;
    private int emailsScanned;
    private int applicationsCreated;
    private int applicationsUpdated;
    private List<String> details = new ArrayList<>();

    public EmailSyncResponse() {}

    public EmailSyncResponse(String status, String message, int emailsScanned,
                             int applicationsCreated, int applicationsUpdated, List<String> details) {
        this.status = status;
        this.message = message;
        this.emailsScanned = emailsScanned;
        this.applicationsCreated = applicationsCreated;
        this.applicationsUpdated = applicationsUpdated;
        this.details = details != null ? details : new ArrayList<>();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String status;
        private String message;
        private int emailsScanned;
        private int applicationsCreated;
        private int applicationsUpdated;
        private List<String> details = new ArrayList<>();

        public Builder status(String status) { this.status = status; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder emailsScanned(int emailsScanned) { this.emailsScanned = emailsScanned; return this; }
        public Builder applicationsCreated(int applicationsCreated) { this.applicationsCreated = applicationsCreated; return this; }
        public Builder applicationsUpdated(int applicationsUpdated) { this.applicationsUpdated = applicationsUpdated; return this; }
        public Builder details(List<String> details) { this.details = details; return this; }

        public EmailSyncResponse build() {
            return new EmailSyncResponse(status, message, emailsScanned, applicationsCreated, applicationsUpdated, details);
        }
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getEmailsScanned() { return emailsScanned; }
    public void setEmailsScanned(int emailsScanned) { this.emailsScanned = emailsScanned; }

    public int getApplicationsCreated() { return applicationsCreated; }
    public void setApplicationsCreated(int applicationsCreated) { this.applicationsCreated = applicationsCreated; }

    public int getApplicationsUpdated() { return applicationsUpdated; }
    public void setApplicationsUpdated(int applicationsUpdated) { this.applicationsUpdated = applicationsUpdated; }

    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }
}
