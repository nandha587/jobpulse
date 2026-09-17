package com.jobpulse.service.impl;

import com.jobpulse.dto.request.ApplicationRequest;
import com.jobpulse.dto.response.ApplicationResponse;
import com.jobpulse.dto.response.PagedResponse;
import com.jobpulse.dto.response.StatusHistoryResponse;
import com.jobpulse.entity.Application;
import com.jobpulse.entity.ApplicationStatusHistory;
import com.jobpulse.entity.User;
import com.jobpulse.entity.enums.ApplicationStatus;
import com.jobpulse.entity.enums.Priority;
import com.jobpulse.exception.ResourceNotFoundException;
import com.jobpulse.mapper.ApplicationMapper;
import com.jobpulse.mapper.StatusHistoryMapper;
import com.jobpulse.repository.ApplicationRepository;
import com.jobpulse.repository.ApplicationStatusHistoryRepository;
import com.jobpulse.repository.UserRepository;
import com.jobpulse.repository.specification.ApplicationSpecification;
import com.jobpulse.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final ApplicationStatusHistoryRepository statusHistoryRepository;
    private final ApplicationMapper applicationMapper;
    private final StatusHistoryMapper statusHistoryMapper;

    @Override
    @Transactional
    public ApplicationResponse createApplication(Long userId, ApplicationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Application application = applicationMapper.toEntity(request, user);
        Application saved = applicationRepository.save(application);

        ApplicationStatusHistory initialHistory = ApplicationStatusHistory.builder()
                .application(saved)
                .previousStatus(null)
                .newStatus(saved.getStatus())
                .build();
        statusHistoryRepository.save(initialHistory);

        return applicationMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getAllApplications(Long userId) {
        return applicationRepository.findByUserIdOrderByApplicationDateDesc(userId)
                .stream()
                .map(applicationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse getApplicationById(Long userId, Long applicationId) {
        Application application = applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        return applicationMapper.toResponse(application);
    }

    @Override
    @Transactional
    public ApplicationResponse updateApplication(Long userId, Long applicationId, ApplicationRequest request) {
        Application application = applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        ApplicationStatus oldStatus = application.getStatus();
        ApplicationStatus newStatus = request.getStatus();

        if (oldStatus != newStatus) {
            recordStatusChange(application, oldStatus, newStatus);
        }

        applicationMapper.updateEntityFromRequest(application, request);
        Application updated = applicationRepository.save(application);
        return applicationMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteApplication(Long userId, Long applicationId) {
        Application application = applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        applicationRepository.delete(application);
    }

    @Override
    @Transactional
    public ApplicationResponse updateStatus(Long userId, Long applicationId, ApplicationStatus newStatus) {
        Application application = applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        ApplicationStatus oldStatus = application.getStatus();

        if (oldStatus != newStatus) {
            recordStatusChange(application, oldStatus, newStatus);
            application.setStatus(newStatus);
            application = applicationRepository.save(application);
        }

        return applicationMapper.toResponse(application);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusHistoryResponse> getStatusHistory(Long userId, Long applicationId) {
        if (!applicationRepository.existsByIdAndUserId(applicationId, userId)) {
            throw new ResourceNotFoundException("Application not found with id: " + applicationId);
        }

        return statusHistoryRepository.findByApplicationIdOrderByChangedAtAsc(applicationId)
                .stream()
                .map(statusHistoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ApplicationResponse> getApplications(
            Long userId,
            String search,
            ApplicationStatus status,
            Priority priority,
            String location,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Application> spec = ApplicationSpecification.filter(
                userId, search, status, priority, location);

        Page<Application> appPage = applicationRepository.findAll(spec, pageable);

        List<ApplicationResponse> content = appPage.getContent()
                .stream()
                .map(applicationMapper::toResponse)
                .toList();

        return PagedResponse.<ApplicationResponse>builder()
                .content(content)
                .pageNumber(appPage.getNumber())
                .pageSize(appPage.getSize())
                .totalElements(appPage.getTotalElements())
                .totalPages(appPage.getTotalPages())
                .last(appPage.isLast())
                .build();
    }

    private void recordStatusChange(Application application, ApplicationStatus oldStatus, ApplicationStatus newStatus) {
        ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                .application(application)
                .previousStatus(oldStatus)
                .newStatus(newStatus)
                .build();
        statusHistoryRepository.save(history);
    }
}
