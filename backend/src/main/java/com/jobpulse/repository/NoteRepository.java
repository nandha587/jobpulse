package com.jobpulse.repository;

import com.jobpulse.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByApplicationIdOrderByCreatedAtDesc(Long applicationId);

    @Query("SELECT n FROM Note n WHERE n.id = :noteId AND n.application.user.id = :userId")
    Optional<Note> findByIdAndUserId(@Param("noteId") Long noteId, @Param("userId") Long userId);
}
