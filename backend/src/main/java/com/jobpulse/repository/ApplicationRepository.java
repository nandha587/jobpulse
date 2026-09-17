package com.jobpulse.repository;

import com.jobpulse.entity.Application;
import com.jobpulse.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {

    List<Application> findByUserIdOrderByApplicationDateDesc(Long userId);

    Optional<Application> findByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    long countByUserId(Long userId);

    long countByUserIdAndApplicationDateGreaterThanEqual(Long userId, LocalDate fromDate);

    long countByUserIdAndStatus(Long userId, ApplicationStatus status);

    @Query("SELECT a.status, COUNT(a) FROM Application a WHERE a.user.id = :userId GROUP BY a.status")
    List<Object[]> countApplicationsByStatusGrouped(@Param("userId") Long userId);

    List<Application> findTop5ByUserIdOrderByApplicationDateDescCreatedAtDesc(Long userId);
}
