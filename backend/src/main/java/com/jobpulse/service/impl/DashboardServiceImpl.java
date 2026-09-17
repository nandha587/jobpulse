package com.jobpulse.service.impl;

import com.jobpulse.dto.response.ApplicationResponse;
import com.jobpulse.dto.response.DashboardStatsResponse;
import com.jobpulse.dto.response.InterviewResponse;
import com.jobpulse.entity.enums.ApplicationStatus;
import com.jobpulse.mapper.ApplicationMapper;
import com.jobpulse.mapper.InterviewMapper;
import com.jobpulse.repository.ApplicationRepository;
import com.jobpulse.repository.InterviewRepository;
import com.jobpulse.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final ApplicationMapper applicationMapper;
    private final InterviewMapper interviewMapper;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats(Long userId) {
        long total = applicationRepository.countByUserId(userId);

        LocalDate startOfMonth = YearMonth.now().atDay(1);
        long thisMonth = applicationRepository.countByUserIdAndApplicationDateGreaterThanEqual(userId, startOfMonth);

        Map<ApplicationStatus, Long> countsMap = new EnumMap<>(ApplicationStatus.class);
        for (ApplicationStatus status : ApplicationStatus.values()) {
            countsMap.put(status, 0L);
        }

        List<Object[]> groupedResults = applicationRepository.countApplicationsByStatusGrouped(userId);
        for (Object[] result : groupedResults) {
            ApplicationStatus status = (ApplicationStatus) result[0];
            Long count = (Long) result[1];
            countsMap.put(status, count);
        }

        long offers = countsMap.getOrDefault(ApplicationStatus.OFFER, 0L);
        long rejections = countsMap.getOrDefault(ApplicationStatus.REJECTED, 0L);
        long inProgress = countsMap.getOrDefault(ApplicationStatus.APPLIED, 0L)
                + countsMap.getOrDefault(ApplicationStatus.ONLINE_TEST, 0L)
                + countsMap.getOrDefault(ApplicationStatus.INTERVIEW, 0L);

        Instant now = Instant.now();
        List<InterviewResponse> upcomingInterviews = interviewRepository
                .findTop5UpcomingInterviews(userId, now)
                .stream()
                .map(interviewMapper::toResponse)
                .toList();

        List<ApplicationResponse> recentApplications = applicationRepository
                .findTop5ByUserIdOrderByApplicationDateDescCreatedAtDesc(userId)
                .stream()
                .map(applicationMapper::toResponse)
                .toList();

        return DashboardStatsResponse.builder()
                .totalApplications(total)
                .appliedThisMonth(thisMonth)
                .activeInterviews(upcomingInterviews.size())
                .offers(offers)
                .rejections(rejections)
                .inProgress(inProgress)
                .statusCounts(countsMap)
                .recentApplications(recentApplications)
                .upcomingInterviews(upcomingInterviews)
                .build();
    }
}
