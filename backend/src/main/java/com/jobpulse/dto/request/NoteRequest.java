package com.jobpulse.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteRequest {
    @NotBlank(message = "Note content cannot be blank")
    private String content;
}
