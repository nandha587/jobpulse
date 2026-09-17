package com.jobpulse.mapper;

import com.jobpulse.dto.response.StatusHistoryResponse;
import com.jobpulse.entity.ApplicationStatusHistory;
import org.springframework.stereotype.Component;

@Component
public class StatusHistoryMapper {
    public StatusHistoryResponse toResponse(ApplicationStatusHistory history) {
        return StatusHistoryResponse.builder()
                .id(history.getId())
                .applicationId(history.getApplication().getId())
                .previousStatus(history.getPreviousStatus())
                .newStatus(history.getNewStatus())
                .changedAt(history.getChangedAt())
                .build();
    }
}
