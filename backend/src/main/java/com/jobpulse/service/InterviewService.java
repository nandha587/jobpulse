package com.jobpulse.service;

import com.jobpulse.dto.request.InterviewRequest;
import com.jobpulse.dto.response.InterviewResponse;

import java.util.List;

public interface InterviewService {
    InterviewResponse scheduleInterview(Long userId, Long applicationId, InterviewRequest request);
    List<InterviewResponse> getInterviewsByApplication(Long userId, Long applicationId);
    InterviewResponse updateInterview(Long userId, Long interviewId, InterviewRequest request);
    void deleteInterview(Long userId, Long interviewId);
}
