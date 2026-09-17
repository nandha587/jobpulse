package com.jobpulse.dto.request;

import com.jobpulse.entity.enums.ApplicationStatus;
import com.jobpulse.entity.enums.JobType;
import com.jobpulse.entity.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationRequest {

    @NotBlank(message = "Company name cannot be blank")
    @Size(max = 150, message = "Company name must not exceed 150 characters")
    private String companyName;

    @NotBlank(message = "Job title cannot be blank")
    @Size(max = 150, message = "Job title must not exceed 150 characters")
    private String jobTitle;

    @Size(max = 150, message = "Location must not exceed 150 characters")
    private String location;

    @NotNull(message = "Job type is required")
    private JobType jobType;

    @NotNull(message = "Application date is required")
    private LocalDate applicationDate;

    @Size(max = 500, message = "Job URL must not exceed 500 characters")
    private String jobUrl;

    @Size(max = 100, message = "Source must not exceed 100 characters")
    private String source;

    @NotNull(message = "Application status is required")
    private ApplicationStatus status;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @Size(max = 100, message = "Salary info must not exceed 100 characters")
    private String salaryInfo;

    private String notesSummary;
}
