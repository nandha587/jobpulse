package com.jobpulse.mapper;

import com.jobpulse.dto.response.NoteResponse;
import com.jobpulse.entity.Application;
import com.jobpulse.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {
    public Note toEntity(String content, Application application) {
        return Note.builder()
                .application(application)
                .content(content.trim())
                .build();
    }

    public NoteResponse toResponse(Note note) {
        return NoteResponse.builder()
                .id(note.getId())
                .applicationId(note.getApplication().getId())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .build();
    }
}
