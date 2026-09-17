package com.jobpulse.dto.response;

import com.jobpulse.entity.enums.InterviewResult;
import com.jobpulse.entity.enums.InterviewType;
import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
