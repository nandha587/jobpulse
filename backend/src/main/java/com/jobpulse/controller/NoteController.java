package com.jobpulse.controller;

import com.jobpulse.dto.request.NoteRequest;
import com.jobpulse.dto.response.NoteResponse;
import com.jobpulse.security.UserPrincipal;
import com.jobpulse.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/applications/{applicationId}/notes")
    public ResponseEntity<NoteResponse> addNote(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long applicationId,
            @Valid @RequestBody NoteRequest request) {
        NoteResponse response = noteService.addNote(principal.getId(), applicationId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/applications/{applicationId}/notes")
    public ResponseEntity<List<NoteResponse>> getNotes(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long applicationId) {
        return ResponseEntity.ok(noteService.getNotesByApplication(principal.getId(), applicationId));
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Void> deleteNote(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        noteService.deleteNote(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
