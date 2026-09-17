package com.jobpulse.mapper;

import com.jobpulse.dto.request.ApplicationRequest;
import com.jobpulse.dto.response.ApplicationResponse;
import com.jobpulse.entity.Application;
import com.jobpulse.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public Application toEntity(ApplicationRequest request, User user) {
        return Application.builder()
                .user(user)
                .companyName(request.getCompanyName().trim())
                .jobTitle(request.getJobTitle().trim())
                .location(request.getLocation() != null ? request.getLocation().trim() : null)
                .jobType(request.getJobType())
                .applicationDate(request.getApplicationDate())
                .jobUrl(request.getJobUrl() != null ? request.getJobUrl().trim() : null)
                .source(request.getSource() != null ? request.getSource().trim() : null)
                .status(request.getStatus())
                .priority(request.getPriority())
                .salaryInfo(request.getSalaryInfo() != null ? request.getSalaryInfo().trim() : null)
                .notesSummary(request.getNotesSummary())
                .build();
    }

    public ApplicationResponse toResponse(Application app) {
        return ApplicationResponse.builder()
                .id(app.getId())
                .companyName(app.getCompanyName())
                .jobTitle(app.getJobTitle())
                .location(app.getLocation())
                .jobType(app.getJobType())
                .applicationDate(app.getApplicationDate())
                .jobUrl(app.getJobUrl())
                .source(app.getSource())
                .status(app.getStatus())
                .priority(app.getPriority())
                .salaryInfo(app.getSalaryInfo())
                .notesSummary(app.getNotesSummary())
                .createdAt(app.getCreatedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }

    public void updateEntityFromRequest(Application app, ApplicationRequest request) {
        app.setCompanyName(request.getCompanyName().trim());
        app.setJobTitle(request.getJobTitle().trim());
        app.setLocation(request.getLocation() != null ? request.getLocation().trim() : null);
        app.setJobType(request.getJobType());
        app.setApplicationDate(request.getApplicationDate());
        app.setJobUrl(request.getJobUrl() != null ? request.getJobUrl().trim() : null);
        app.setSource(request.getSource() != null ? request.getSource().trim() : null);
        app.setStatus(request.getStatus());
        app.setPriority(request.getPriority());
        app.setSalaryInfo(request.getSalaryInfo() != null ? request.getSalaryInfo().trim() : null);
        app.setNotesSummary(request.getNotesSummary());
    }
}
