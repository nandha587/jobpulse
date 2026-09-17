package com.jobpulse.mapper;

import com.jobpulse.dto.request.InterviewRequest;
import com.jobpulse.dto.response.InterviewResponse;
import com.jobpulse.entity.Application;
import com.jobpulse.entity.Interview;
import org.springframework.stereotype.Component;

@Component
public class InterviewMapper {

    public Interview toEntity(InterviewRequest request, Application application) {
        return Interview.builder()
                .application(application)
                .interviewDate(request.getInterviewDate())
                .interviewType(request.getInterviewType())
                .interviewer(request.getInterviewer() != null ? request.getInterviewer().trim() : null)
                .notes(request.getNotes())
                .result(request.getResult())
                .build();
    }

    public InterviewResponse toResponse(Interview interview) {
        return InterviewResponse.builder()
                .id(interview.getId())
                .applicationId(interview.getApplication().getId())
                .companyName(interview.getApplication().getCompanyName())
                .jobTitle(interview.getApplication().getJobTitle())
                .interviewDate(interview.getInterviewDate())
                .interviewType(interview.getInterviewType())
                .interviewer(interview.getInterviewer())
                .notes(interview.getNotes())
                .result(interview.getResult())
                .createdAt(interview.getCreatedAt())
                .build();
    }

    public void updateEntityFromRequest(Interview interview, InterviewRequest request) {
        interview.setInterviewDate(request.getInterviewDate());
        interview.setInterviewType(request.getInterviewType());
        interview.setInterviewer(request.getInterviewer() != null ? request.getInterviewer().trim() : null);
        interview.setNotes(request.getNotes());
        interview.setResult(request.getResult());
    }
}
