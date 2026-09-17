package com.jobpulse.service;

import com.jobpulse.dto.response.DashboardStatsResponse;

public interface DashboardService {
    DashboardStatsResponse getDashboardStats(Long userId);
}
