package com.jobpulse.dto.request;

import com.jobpulse.entity.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {
    @NotNull(message = "Status cannot be null")
    private ApplicationStatus status;

    public StatusUpdateRequest() {}
    public StatusUpdateRequest(ApplicationStatus status) { this.status = status; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
}
