package com.jobpulse.dto.request;

import com.jobpulse.entity.enums.InterviewResult;
import com.jobpulse.entity.enums.InterviewType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public class InterviewRequest {

    @NotNull(message = "Interview date and time is required")
    private Instant interviewDate;

    @NotNull(message = "Interview type is required")
    private InterviewType interviewType;

    @Size(max = 150, message = "Interviewer name must not exceed 150 characters")
    private String interviewer;

    private String notes;

    @NotNull(message = "Interview result is required")
    private InterviewResult result = InterviewResult.SCHEDULED;

    public InterviewRequest() {}

    public InterviewRequest(Instant interviewDate, InterviewType interviewType, String interviewer, String notes, InterviewResult result) {
        this.interviewDate = interviewDate;
        this.interviewType = interviewType;
        this.interviewer = interviewer;
        this.notes = notes;
        this.result = result != null ? result : InterviewResult.SCHEDULED;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Instant interviewDate;
        private InterviewType interviewType;
        private String interviewer;
        private String notes;
        private InterviewResult result = InterviewResult.SCHEDULED;

        public Builder interviewDate(Instant interviewDate) { this.interviewDate = interviewDate; return this; }
        public Builder interviewType(InterviewType interviewType) { this.interviewType = interviewType; return this; }
        public Builder interviewer(String interviewer) { this.interviewer = interviewer; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder result(InterviewResult result) { this.result = result; return this; }
        public InterviewRequest build() { return new InterviewRequest(interviewDate, interviewType, interviewer, notes, result); }
    }

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
}
