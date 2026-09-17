package com.jobpulse.dto.response;

import com.jobpulse.entity.enums.ApplicationStatus;
import lombok.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {
    private long totalApplications;
    private long appliedThisMonth;
    private long activeInterviews;
    private long offers;
    private long rejections;
    private long inProgress;
    private Map<ApplicationStatus, Long> statusCounts;
    private List<ApplicationResponse> recentApplications;
    private List<InterviewResponse> upcomingInterviews;
}
