package com.jobpulse.controller;

import com.jobpulse.dto.request.EmailConfigRequest;
import com.jobpulse.dto.request.EmailSimulateRequest;
import com.jobpulse.dto.response.EmailConfigResponse;
import com.jobpulse.dto.response.EmailSyncResponse;
import com.jobpulse.security.UserPrincipal;
import com.jobpulse.service.EmailSyncService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email-sync")
public class EmailSyncController {

    private final EmailSyncService emailSyncService;

    public EmailSyncController(EmailSyncService emailSyncService) {
        this.emailSyncService = emailSyncService;
    }

    @GetMapping("/config")
    public ResponseEntity<EmailConfigResponse> getConfig(@AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(emailSyncService.getConfig(currentUser.getId()));
    }

    @PostMapping("/config")
    public ResponseEntity<EmailConfigResponse> saveConfig(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody EmailConfigRequest request) {
        return ResponseEntity.ok(emailSyncService.saveConfig(currentUser.getId(), request));
    }

    @PostMapping("/test-connection")
    public ResponseEntity<Map<String, Object>> testConnection(@AuthenticationPrincipal UserPrincipal currentUser) {
        boolean success = emailSyncService.testConnection(currentUser.getId());
        return ResponseEntity.ok(Map.of("success", success, "message", "Connected successfully to mail server."));
    }

    @PostMapping("/sync")
    public ResponseEntity<EmailSyncResponse> syncEmails(@AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(emailSyncService.syncUserEmails(currentUser.getId()));
    }

    @PostMapping("/simulate")
    public ResponseEntity<EmailSyncResponse> simulateEmail(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody EmailSimulateRequest request) {
        return ResponseEntity.ok(emailSyncService.simulateEmailIngestion(currentUser.getId(), request));
    }
}
