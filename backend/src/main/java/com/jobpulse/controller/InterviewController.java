package com.jobpulse.controller;

import com.jobpulse.dto.request.InterviewRequest;
import com.jobpulse.dto.response.InterviewResponse;
import com.jobpulse.security.UserPrincipal;
import com.jobpulse.service.InterviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/applications/{applicationId}/interviews")
    public ResponseEntity<InterviewResponse> scheduleInterview(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long applicationId,
            @Valid @RequestBody InterviewRequest request) {
        InterviewResponse response = interviewService.scheduleInterview(principal.getId(), applicationId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/applications/{applicationId}/interviews")
    public ResponseEntity<List<InterviewResponse>> getInterviews(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long applicationId) {
        return ResponseEntity.ok(interviewService.getInterviewsByApplication(principal.getId(), applicationId));
    }

    @PutMapping("/interviews/{id}")
    public ResponseEntity<InterviewResponse> updateInterview(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody InterviewRequest request) {
        return ResponseEntity.ok(interviewService.updateInterview(principal.getId(), id, request));
    }

    @DeleteMapping("/interviews/{id}")
    public ResponseEntity<Void> deleteInterview(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        interviewService.deleteInterview(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
