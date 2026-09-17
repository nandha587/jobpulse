package com.jobpulse.dto.response;

import com.jobpulse.entity.enums.ApplicationStatus;
import java.util.List;
import java.util.Map;

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

    public DashboardStatsResponse() {}

    public DashboardStatsResponse(long totalApplications, long appliedThisMonth, long activeInterviews,
                                  long offers, long rejections, long inProgress, Map<ApplicationStatus, Long> statusCounts,
                                  List<ApplicationResponse> recentApplications, List<InterviewResponse> upcomingInterviews) {
        this.totalApplications = totalApplications;
        this.appliedThisMonth = appliedThisMonth;
        this.activeInterviews = activeInterviews;
        this.offers = offers;
        this.rejections = rejections;
        this.inProgress = inProgress;
        this.statusCounts = statusCounts;
        this.recentApplications = recentApplications;
        this.upcomingInterviews = upcomingInterviews;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long totalApplications;
        private long appliedThisMonth;
        private long activeInterviews;
        private long offers;
        private long rejections;
        private long inProgress;
        private Map<ApplicationStatus, Long> statusCounts;
        private List<ApplicationResponse> recentApplications;
        private List<InterviewResponse> upcomingInterviews;

        public Builder totalApplications(long totalApplications) { this.totalApplications = totalApplications; return this; }
        public Builder appliedThisMonth(long appliedThisMonth) { this.appliedThisMonth = appliedThisMonth; return this; }
        public Builder activeInterviews(long activeInterviews) { this.activeInterviews = activeInterviews; return this; }
        public Builder offers(long offers) { this.offers = offers; return this; }
        public Builder rejections(long rejections) { this.rejections = rejections; return this; }
        public Builder inProgress(long inProgress) { this.inProgress = inProgress; return this; }
        public Builder statusCounts(Map<ApplicationStatus, Long> statusCounts) { this.statusCounts = statusCounts; return this; }
        public Builder recentApplications(List<ApplicationResponse> recentApplications) { this.recentApplications = recentApplications; return this; }
        public Builder upcomingInterviews(List<InterviewResponse> upcomingInterviews) { this.upcomingInterviews = upcomingInterviews; return this; }
        public DashboardStatsResponse build() {
            return new DashboardStatsResponse(totalApplications, appliedThisMonth, activeInterviews, offers, rejections, inProgress, statusCounts, recentApplications, upcomingInterviews);
        }
    }

    public long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }
    public long getAppliedThisMonth() { return appliedThisMonth; }
    public void setAppliedThisMonth(long appliedThisMonth) { this.appliedThisMonth = appliedThisMonth; }
    public long getActiveInterviews() { return activeInterviews; }
    public void setActiveInterviews(long activeInterviews) { this.activeInterviews = activeInterviews; }
    public long getOffers() { return offers; }
    public void setOffers(long offers) { this.offers = offers; }
    public long getRejections() { return rejections; }
    public void setRejections(long rejections) { this.rejections = rejections; }
    public long getInProgress() { return inProgress; }
    public void setInProgress(long inProgress) { this.inProgress = inProgress; }
    public Map<ApplicationStatus, Long> getStatusCounts() { return statusCounts; }
    public void setStatusCounts(Map<ApplicationStatus, Long> statusCounts) { this.statusCounts = statusCounts; }
    public List<ApplicationResponse> getRecentApplications() { return recentApplications; }
    public void setRecentApplications(List<ApplicationResponse> recentApplications) { this.recentApplications = recentApplications; }
    public List<InterviewResponse> getUpcomingInterviews() { return upcomingInterviews; }
    public void setUpcomingInterviews(List<InterviewResponse> upcomingInterviews) { this.upcomingInterviews = upcomingInterviews; }
}
