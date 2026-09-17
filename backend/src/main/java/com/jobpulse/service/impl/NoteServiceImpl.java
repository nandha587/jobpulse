package com.jobpulse.service.impl;

import com.jobpulse.dto.request.NoteRequest;
import com.jobpulse.dto.response.NoteResponse;
import com.jobpulse.entity.Application;
import com.jobpulse.entity.Note;
import com.jobpulse.exception.ResourceNotFoundException;
import com.jobpulse.mapper.NoteMapper;
import com.jobpulse.repository.ApplicationRepository;
import com.jobpulse.repository.NoteRepository;
import com.jobpulse.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final ApplicationRepository applicationRepository;
    private final NoteMapper noteMapper;

    @Override
    @Transactional
    public NoteResponse addNote(Long userId, Long applicationId, NoteRequest request) {
        Application application = applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        Note note = noteMapper.toEntity(request.getContent(), application);
        Note saved = noteRepository.save(note);
        return noteMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponse> getNotesByApplication(Long userId, Long applicationId) {
        if (!applicationRepository.existsByIdAndUserId(applicationId, userId)) {
            throw new ResourceNotFoundException("Application not found with id: " + applicationId);
        }

        return noteRepository.findByApplicationIdOrderByCreatedAtDesc(applicationId)
                .stream()
                .map(noteMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteNote(Long userId, Long noteId) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + noteId));

        noteRepository.delete(note);
    }
}
