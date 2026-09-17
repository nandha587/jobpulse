package com.jobpulse.entity;

import com.jobpulse.entity.enums.ApplicationStatus;
import com.jobpulse.entity.enums.JobType;
import com.jobpulse.entity.enums.Priority;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name = "job_title", nullable = false, length = 150)
    private String jobTitle;

    @Column(length = 150)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false, length = 20)
    private JobType jobType = JobType.FULL_TIME;

    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;

    @Column(name = "job_url", length = 500)
    private String jobUrl;

    @Column(length = 100)
    private String source;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Priority priority = Priority.MEDIUM;

    @Column(name = "salary_info", length = 100)
    private String salaryInfo;

    @Column(name = "notes_summary", columnDefinition = "TEXT")
    private String notesSummary;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Application() {}

    public Application(Long id, User user, String companyName, String jobTitle, String location,
                       JobType jobType, LocalDate applicationDate, String jobUrl, String source,
                       ApplicationStatus status, Priority priority, String salaryInfo,
                       String notesSummary, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.user = user;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.location = location;
        this.jobType = jobType != null ? jobType : JobType.FULL_TIME;
        this.applicationDate = applicationDate;
        this.jobUrl = jobUrl;
        this.source = source;
        this.status = status != null ? status : ApplicationStatus.APPLIED;
        this.priority = priority != null ? priority : Priority.MEDIUM;
        this.salaryInfo = salaryInfo;
        this.notesSummary = notesSummary;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private String companyName;
        private String jobTitle;
        private String location;
        private JobType jobType = JobType.FULL_TIME;
        private LocalDate applicationDate;
        private String jobUrl;
        private String source;
        private ApplicationStatus status = ApplicationStatus.APPLIED;
        private Priority priority = Priority.MEDIUM;
        private String salaryInfo;
        private String notesSummary;
        private Instant createdAt;
        private Instant updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
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
        public Application build() {
            return new Application(id, user, companyName, jobTitle, location, jobType, applicationDate, jobUrl, source, status, priority, salaryInfo, notesSummary, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
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
