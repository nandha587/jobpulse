package com.jobpulse.entity;

import com.jobpulse.entity.enums.InterviewResult;
import com.jobpulse.entity.enums.InterviewType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Entity
@Table(name = "interviews")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "interview_date", nullable = false)
    private Instant interviewDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_type", nullable = false, length = 30)
    private InterviewType interviewType;

    @Column(length = 150)
    private String interviewer;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InterviewResult result = InterviewResult.SCHEDULED;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Interview() {}

    public Interview(Long id, Application application, Instant interviewDate, InterviewType interviewType,
                     String interviewer, String notes, InterviewResult result, Instant createdAt) {
        this.id = id;
        this.application = application;
        this.interviewDate = interviewDate;
        this.interviewType = interviewType;
        this.interviewer = interviewer;
        this.notes = notes;
        this.result = result != null ? result : InterviewResult.SCHEDULED;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Application application;
        private Instant interviewDate;
        private InterviewType interviewType;
        private String interviewer;
        private String notes;
        private InterviewResult result = InterviewResult.SCHEDULED;
        private Instant createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder application(Application application) { this.application = application; return this; }
        public Builder interviewDate(Instant interviewDate) { this.interviewDate = interviewDate; return this; }
        public Builder interviewType(InterviewType interviewType) { this.interviewType = interviewType; return this; }
        public Builder interviewer(String interviewer) { this.interviewer = interviewer; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder result(InterviewResult result) { this.result = result; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Interview build() {
            return new Interview(id, application, interviewDate, interviewType, interviewer, notes, result, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }
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
