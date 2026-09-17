package com.jobpulse.dto.response;

import com.jobpulse.entity.enums.InterviewResult;
import com.jobpulse.entity.enums.InterviewType;
import java.time.Instant;

public class InterviewResponse {
    private Long id;
    private Long applicationId;
    private String companyName;
    private String jobTitle;
    private Instant interviewDate;
    private InterviewType interviewType;
    private String interviewer;
    private String notes;
    private InterviewResult result;
    private Instant createdAt;

    public InterviewResponse() {}

    public InterviewResponse(Long id, Long applicationId, String companyName, String jobTitle,
                             Instant interviewDate, InterviewType interviewType, String interviewer,
                             String notes, InterviewResult result, Instant createdAt) {
        this.id = id;
        this.applicationId = applicationId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.interviewDate = interviewDate;
        this.interviewType = interviewType;
        this.interviewer = interviewer;
        this.notes = notes;
        this.result = result;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long applicationId;
        private String companyName;
        private String jobTitle;
        private Instant interviewDate;
        private InterviewType interviewType;
        private String interviewer;
        private String notes;
        private InterviewResult result;
        private Instant createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder applicationId(Long applicationId) { this.applicationId = applicationId; return this; }
        public Builder companyName(String companyName) { this.companyName = companyName; return this; }
        public Builder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
        public Builder interviewDate(Instant interviewDate) { this.interviewDate = interviewDate; return this; }
        public Builder interviewType(InterviewType interviewType) { this.interviewType = interviewType; return this; }
        public Builder interviewer(String interviewer) { this.interviewer = interviewer; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder result(InterviewResult result) { this.result = result; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public InterviewResponse build() {
            return new InterviewResponse(id, applicationId, companyName, jobTitle, interviewDate, interviewType, interviewer, notes, result, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public Instant getInterviewDate() { return interviewDate; }
    public void setInterviewDate(Instant interviewDate) { this.interviewDate = interviewDate; }
    public InterviewType getInterviewType() { return interviewType; }
    public void setInterviewType(InterviewType interviewType) { this.interviewType = interviewType; }
    public String getInterviewer() { return interviewer; }
    public void setInterviewer(String interviewer) { this.interviewer = interviewer; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public InterviewResult getResult() { return result; }
    public void setResult(InterviewResult result) { this.result = result; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
