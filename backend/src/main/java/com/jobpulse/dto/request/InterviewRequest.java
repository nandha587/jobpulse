package com.jobpulse.dto.request;

import com.jobpulse.entity.enums.InterviewResult;
import com.jobpulse.entity.enums.InterviewType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewRequest {

    @NotNull(message = "Interview date and time is required")
    private Instant interviewDate;

    @NotNull(message = "Interview type is required")
    private InterviewType interviewType;

    @Size(max = 150, message = "Interviewer name must not exceed 150 characters")
    private String interviewer;

    private String notes;

    @NotNull(message = "Interview result is required")
    @Builder.Default
    private InterviewResult result = InterviewResult.SCHEDULED;
}
