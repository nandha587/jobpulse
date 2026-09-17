package com.jobpulse.repository;

import com.jobpulse.entity.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailConfigRepository extends JpaRepository<EmailConfig, Long> {
    Optional<EmailConfig> findByUserId(Long userId);
    List<EmailConfig> findByAutoSyncEnabledTrue();
    void deleteByUserId(Long userId);
}
