package com.jobpulse.dto.response;

import com.jobpulse.entity.enums.ApplicationStatus;
import java.time.Instant;

public class StatusHistoryResponse {
    private Long id;
    private Long applicationId;
    private ApplicationStatus previousStatus;
    private ApplicationStatus newStatus;
    private Instant changedAt;

    public StatusHistoryResponse() {}

    public StatusHistoryResponse(Long id, Long applicationId, ApplicationStatus previousStatus, ApplicationStatus newStatus, Instant changedAt) {
        this.id = id;
        this.applicationId = applicationId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedAt = changedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long applicationId;
        private ApplicationStatus previousStatus;
        private ApplicationStatus newStatus;
        private Instant changedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder applicationId(Long applicationId) { this.applicationId = applicationId; return this; }
        public Builder previousStatus(ApplicationStatus previousStatus) { this.previousStatus = previousStatus; return this; }
        public Builder newStatus(ApplicationStatus newStatus) { this.newStatus = newStatus; return this; }
        public Builder changedAt(Instant changedAt) { this.changedAt = changedAt; return this; }
        public StatusHistoryResponse build() { return new StatusHistoryResponse(id, applicationId, previousStatus, newStatus, changedAt); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public ApplicationStatus getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(ApplicationStatus previousStatus) { this.previousStatus = previousStatus; }
    public ApplicationStatus getNewStatus() { return newStatus; }
    public void setNewStatus(ApplicationStatus newStatus) { this.newStatus = newStatus; }
    public Instant getChangedAt() { return changedAt; }
    public void setChangedAt(Instant changedAt) { this.changedAt = changedAt; }
}
