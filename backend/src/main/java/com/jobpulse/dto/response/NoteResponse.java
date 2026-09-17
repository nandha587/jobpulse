package com.jobpulse.dto.response;

import java.time.Instant;

public class NoteResponse {
    private Long id;
    private Long applicationId;
    private String content;
    private Instant createdAt;

    public NoteResponse() {}

    public NoteResponse(Long id, Long applicationId, String content, Instant createdAt) {
        this.id = id;
        this.applicationId = applicationId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long applicationId;
        private String content;
        private Instant createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder applicationId(Long applicationId) { this.applicationId = applicationId; return this; }
        public Builder content(String content) { this.content = content; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public NoteResponse build() { return new NoteResponse(id, applicationId, content, createdAt); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
