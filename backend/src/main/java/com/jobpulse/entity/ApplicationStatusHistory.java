package com.jobpulse.entity;

import com.jobpulse.entity.enums.ApplicationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Entity
@Table(name = "application_status_history")
public class ApplicationStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", length = 30)
    private ApplicationStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 30)
    private ApplicationStatus newStatus;

    @CreationTimestamp
    @Column(name = "changed_at", nullable = false, updatable = false)
    private Instant changedAt;

    public ApplicationStatusHistory() {}

    public ApplicationStatusHistory(Long id, Application application, ApplicationStatus previousStatus, ApplicationStatus newStatus, Instant changedAt) {
        this.id = id;
        this.application = application;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedAt = changedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Application application;
        private ApplicationStatus previousStatus;
        private ApplicationStatus newStatus;
        private Instant changedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder application(Application application) { this.application = application; return this; }
        public Builder previousStatus(ApplicationStatus previousStatus) { this.previousStatus = previousStatus; return this; }
        public Builder newStatus(ApplicationStatus newStatus) { this.newStatus = newStatus; return this; }
        public Builder changedAt(Instant changedAt) { this.changedAt = changedAt; return this; }
        public ApplicationStatusHistory build() {
            return new ApplicationStatusHistory(id, application, previousStatus, newStatus, changedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }
    public ApplicationStatus getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(ApplicationStatus previousStatus) { this.previousStatus = previousStatus; }
    public ApplicationStatus getNewStatus() { return newStatus; }
    public void setNewStatus(ApplicationStatus newStatus) { this.newStatus = newStatus; }
    public Instant getChangedAt() { return changedAt; }
    public void setChangedAt(Instant changedAt) { this.changedAt = changedAt; }
}
