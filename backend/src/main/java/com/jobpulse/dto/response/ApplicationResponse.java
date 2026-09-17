package com.jobpulse.dto.response;

import com.jobpulse.entity.enums.ApplicationStatus;
import com.jobpulse.entity.enums.JobType;
import com.jobpulse.entity.enums.Priority;
import java.time.Instant;
import java.time.LocalDate;

public class ApplicationResponse {
    private Long id;
    private String companyName;
    private String jobTitle;
    private String location;
    private JobType jobType;
    private LocalDate applicationDate;
    private String jobUrl;
    private String source;
    private ApplicationStatus status;
    private Priority priority;
    private String salaryInfo;
    private String notesSummary;
    private Instant createdAt;
    private Instant updatedAt;

    public ApplicationResponse() {}

    public ApplicationResponse(Long id, String companyName, String jobTitle, String location,
                               JobType jobType, LocalDate applicationDate, String jobUrl, String source,
                               ApplicationStatus status, Priority priority, String salaryInfo,
                               String notesSummary, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.location = location;
        this.jobType = jobType;
        this.applicationDate = applicationDate;
        this.jobUrl = jobUrl;
        this.source = source;
        this.status = status;
        this.priority = priority;
        this.salaryInfo = salaryInfo;
        this.notesSummary = notesSummary;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String companyName;
        private String jobTitle;
        private String location;
        private JobType jobType;
        private LocalDate applicationDate;
        private String jobUrl;
        private String source;
        private ApplicationStatus status;
        private Priority priority;
        private String salaryInfo;
        private String notesSummary;
        private Instant createdAt;
        private Instant updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder companyName(String companyName) { this.companyName = companyName; return this; }
        public Builder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder jobType(JobType jobType) { this.jobType = jobType; return this; }
        public Builder applicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; return this; }
        public Builder jobUrl(String jobUrl) { this.jobUrl = jobUrl; return this; }
        public Builder source(String source) { this.source = source; return this; }
        public Builder status(ApplicationStatus status) { this.status = status; return this; }
        public Builder priority(Priority priority) { this.priority = priority; return this; }
        public Builder salaryInfo(String salaryInfo) { this.salaryInfo = salaryInfo; return this; }
        public Builder notesSummary(String notesSummary) { this.notesSummary = notesSummary; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }
        public ApplicationResponse build() {
            return new ApplicationResponse(id, companyName, jobTitle, location, jobType, applicationDate, jobUrl, source, status, priority, salaryInfo, notesSummary, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public JobType getJobType() { return jobType; }
    public void setJobType(JobType jobType) { this.jobType = jobType; }
    public LocalDate getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; }
    public String getJobUrl() { return jobUrl; }
    public void setJobUrl(String jobUrl) { this.jobUrl = jobUrl; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public String getSalaryInfo() { return salaryInfo; }
    public void setSalaryInfo(String salaryInfo) { this.salaryInfo = salaryInfo; }
    public String getNotesSummary() { return notesSummary; }
    public void setNotesSummary(String notesSummary) { this.notesSummary = notesSummary; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
