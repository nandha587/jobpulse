package com.jobpulse.dto.response;

import com.jobpulse.entity.enums.ApplicationStatus;
import com.jobpulse.entity.enums.JobType;
import com.jobpulse.entity.enums.Priority;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponse {
    private Long id;
    private String companyName;
    private String jobTitle;
    private String location;
    private JobType jobType;
    private LocalDate applicationDate;
    private String jobUrl;
    private String source;
    private ApplicationStatus status;
    private Priority priority;
    private String salaryInfo;
    private String notesSummary;
    private Instant createdAt;
    private Instant updatedAt;
}
