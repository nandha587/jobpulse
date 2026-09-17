package com.jobpulse.service;

import com.jobpulse.dto.request.NoteRequest;
import com.jobpulse.dto.response.NoteResponse;

import java.util.List;

public interface NoteService {
    NoteResponse addNote(Long userId, Long applicationId, NoteRequest request);
    List<NoteResponse> getNotesByApplication(Long userId, Long applicationId);
    void deleteNote(Long userId, Long noteId);
}
