package com.jobpulse.service.impl;

import com.jobpulse.dto.request.InterviewRequest;
import com.jobpulse.dto.response.InterviewResponse;
import com.jobpulse.entity.Application;
import com.jobpulse.entity.Interview;
import com.jobpulse.entity.enums.ApplicationStatus;
import com.jobpulse.exception.ResourceNotFoundException;
import com.jobpulse.mapper.InterviewMapper;
import com.jobpulse.repository.ApplicationRepository;
import com.jobpulse.repository.InterviewRepository;
import com.jobpulse.service.ApplicationService;
import com.jobpulse.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationService applicationService;
    private final InterviewMapper interviewMapper;

    @Override
    @Transactional
    public InterviewResponse scheduleInterview(Long userId, Long applicationId, InterviewRequest request) {
        Application application = applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        Interview interview = interviewMapper.toEntity(request, application);
        Interview saved = interviewRepository.save(interview);

        if (application.getStatus() == ApplicationStatus.APPLIED || application.getStatus() == ApplicationStatus.ONLINE_TEST) {
            applicationService.updateStatus(userId, applicationId, ApplicationStatus.INTERVIEW);
        }

        return interviewMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponse> getInterviewsByApplication(Long userId, Long applicationId) {
        if (!applicationRepository.existsByIdAndUserId(applicationId, userId)) {
            throw new ResourceNotFoundException("Application not found with id: " + applicationId);
        }

        return interviewRepository.findByApplicationIdOrderByInterviewDateAsc(applicationId)
                .stream()
                .map(interviewMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public InterviewResponse updateInterview(Long userId, Long interviewId, InterviewRequest request) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + interviewId));

        interviewMapper.updateEntityFromRequest(interview, request);
        Interview updated = interviewRepository.save(interview);
        return interviewMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteInterview(Long userId, Long interviewId) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + interviewId));

        interviewRepository.delete(interview);
    }
}
