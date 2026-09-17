package com.jobpulse.dto.request;

import com.jobpulse.entity.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateRequest {
    @NotNull(message = "Status cannot be null")
    private ApplicationStatus status;
}
