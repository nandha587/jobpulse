package com.jobpulse.dto;

import com.jobpulse.entity.enums.ApplicationStatus;
import java.time.LocalDate;

public class ParsedApplicationEmail {
    private boolean valid;
    private String companyName;
    private String jobTitle;
    private String location;
    private String source;
    private ApplicationStatus status = ApplicationStatus.APPLIED;
    private LocalDate applicationDate = LocalDate.now();
    private String salaryInfo;
    private String jobUrl;
    private String notes;
    private boolean statusUpdate;

    public ParsedApplicationEmail() {}

    public ParsedApplicationEmail(boolean valid, String companyName, String jobTitle, String location,
                                  String source, ApplicationStatus status, LocalDate applicationDate,
                                  String salaryInfo, String jobUrl, String notes, boolean statusUpdate) {
        this.valid = valid;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.location = location;
        this.source = source;
        this.status = status;
        this.applicationDate = applicationDate;
        this.salaryInfo = salaryInfo;
        this.jobUrl = jobUrl;
        this.notes = notes;
        this.statusUpdate = statusUpdate;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private boolean valid = true;
        private String companyName;
        private String jobTitle;
        private String location;
        private String source;
        private ApplicationStatus status = ApplicationStatus.APPLIED;
        private LocalDate applicationDate = LocalDate.now();
        private String salaryInfo;
        private String jobUrl;
        private String notes;
        private boolean statusUpdate;

        public Builder valid(boolean valid) { this.valid = valid; return this; }
        public Builder companyName(String companyName) { this.companyName = companyName; return this; }
        public Builder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder source(String source) { this.source = source; return this; }
        public Builder status(ApplicationStatus status) { this.status = status; return this; }
        public Builder applicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; return this; }
        public Builder salaryInfo(String salaryInfo) { this.salaryInfo = salaryInfo; return this; }
        public Builder jobUrl(String jobUrl) { this.jobUrl = jobUrl; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder statusUpdate(boolean statusUpdate) { this.statusUpdate = statusUpdate; return this; }

        public ParsedApplicationEmail build() {
            return new ParsedApplicationEmail(valid, companyName, jobTitle, location, source, status, applicationDate, salaryInfo, jobUrl, notes, statusUpdate);
        }
    }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public LocalDate getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; }

    public String getSalaryInfo() { return salaryInfo; }
    public void setSalaryInfo(String salaryInfo) { this.salaryInfo = salaryInfo; }

    public String getJobUrl() { return jobUrl; }
    public void setJobUrl(String jobUrl) { this.jobUrl = jobUrl; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isStatusUpdate() { return statusUpdate; }
    public void setStatusUpdate(boolean statusUpdate) { this.statusUpdate = statusUpdate; }
}
