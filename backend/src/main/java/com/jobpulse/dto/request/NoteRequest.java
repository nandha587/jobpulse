package com.jobpulse.dto.request;

import jakarta.validation.constraints.NotBlank;

public class NoteRequest {
    @NotBlank(message = "Note content cannot be blank")
    private String content;

    public NoteRequest() {}
    public NoteRequest(String content) { this.content = content; }
    public static NoteRequest builder() { return new NoteRequest(); }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
