package com.jobpulse.dto.request;

import jakarta.validation.constraints.NotBlank;

public class EmailSimulateRequest {
    @NotBlank(message = "Sender is required")
    private String sender;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String body;

    public EmailSimulateRequest() {}

    public EmailSimulateRequest(String sender, String subject, String body) {
        this.sender = sender;
        this.subject = subject;
        this.body = body;
    }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}
