package com.jobpulse.repository;

import com.jobpulse.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByApplicationIdOrderByInterviewDateAsc(Long applicationId);

    @Query("SELECT i FROM Interview i WHERE i.id = :interviewId AND i.application.user.id = :userId")
    Optional<Interview> findByIdAndUserId(@Param("interviewId") Long interviewId, @Param("userId") Long userId);

    @Query("SELECT i FROM Interview i WHERE i.application.user.id = :userId " +
           "AND i.interviewDate >= :now AND i.result = 'SCHEDULED' " +
           "ORDER BY i.interviewDate ASC")
    List<Interview> findTop5UpcomingInterviews(@Param("userId") Long userId, @Param("now") Instant now);
}
