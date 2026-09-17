package com.jobpulse.service;

import com.jobpulse.dto.request.ApplicationRequest;
import com.jobpulse.dto.response.ApplicationResponse;
import com.jobpulse.dto.response.PagedResponse;
import com.jobpulse.dto.response.StatusHistoryResponse;
import com.jobpulse.entity.enums.ApplicationStatus;
import com.jobpulse.entity.enums.Priority;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse createApplication(Long userId, ApplicationRequest request);
    List<ApplicationResponse> getAllApplications(Long userId);
    ApplicationResponse getApplicationById(Long userId, Long applicationId);
    ApplicationResponse updateApplication(Long userId, Long applicationId, ApplicationRequest request);
    void deleteApplication(Long userId, Long applicationId);
    ApplicationResponse updateStatus(Long userId, Long applicationId, ApplicationStatus newStatus);
    List<StatusHistoryResponse> getStatusHistory(Long userId, Long applicationId);
    PagedResponse<ApplicationResponse> getApplications(
            Long userId,
            String search,
            ApplicationStatus status,
            Priority priority,
            String location,
            int page,
            int size,
            String sortBy,
            String sortDir);
}
