package com.jobpulse.service;

import com.jobpulse.dto.request.EmailConfigRequest;
import com.jobpulse.dto.request.EmailSimulateRequest;
import com.jobpulse.dto.response.EmailConfigResponse;
import com.jobpulse.dto.response.EmailSyncResponse;

public interface EmailSyncService {
    EmailConfigResponse getConfig(Long userId);
    EmailConfigResponse saveConfig(Long userId, EmailConfigRequest request);
    boolean testConnection(Long userId);
    EmailSyncResponse syncUserEmails(Long userId);
    EmailSyncResponse simulateEmailIngestion(Long userId, EmailSimulateRequest request);
    void runScheduledSync();
}
