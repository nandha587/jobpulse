package com.jobpulse.service;

import com.jobpulse.dto.request.ApplicationRequest;
import com.jobpulse.dto.response.ApplicationResponse;
import com.jobpulse.entity.Application;
import com.jobpulse.entity.User;
import com.jobpulse.entity.enums.ApplicationStatus;
import com.jobpulse.entity.enums.JobType;
import com.jobpulse.entity.enums.Priority;
import com.jobpulse.exception.ResourceNotFoundException;
import com.jobpulse.mapper.ApplicationMapper;
import com.jobpulse.repository.ApplicationRepository;
import com.jobpulse.repository.ApplicationStatusHistoryRepository;
import com.jobpulse.repository.UserRepository;
import com.jobpulse.service.impl.ApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationStatusHistoryRepository statusHistoryRepository;

    @Spy
    private ApplicationMapper applicationMapper = new ApplicationMapper();

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    private User sampleUser;
    private Application sampleApp;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder().id(1L).name("Jane").email("jane@test.com").build();

        sampleApp = Application.builder()
                .id(100L)
                .user(sampleUser)
                .companyName("Acme Corp")
                .jobTitle("Backend Engineer")
                .applicationDate(LocalDate.now())
                .jobType(JobType.FULL_TIME)
                .status(ApplicationStatus.APPLIED)
                .priority(Priority.HIGH)
                .build();
    }

    @Test
    @DisplayName("Should create application and automatically record initial status history")
    void testCreateApplicationLogsInitialHistory() {
        ApplicationRequest request = ApplicationRequest.builder()
                .companyName("Acme Corp")
                .jobTitle("Backend Engineer")
                .applicationDate(LocalDate.now())
                .jobType(JobType.FULL_TIME)
                .status(ApplicationStatus.APPLIED)
                .priority(Priority.HIGH)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(applicationRepository.save(any(Application.class))).thenReturn(sampleApp);

        ApplicationResponse response = applicationService.createApplication(1L, request);

        assertThat(response).isNotNull();
        assertThat(response.getCompanyName()).isEqualTo("Acme Corp");

        verify(statusHistoryRepository).save(argThat(h ->
                h.getPreviousStatus() == null && h.getNewStatus() == ApplicationStatus.APPLIED
        ));
    }

    @Test
    @DisplayName("Tenant Isolation: Should throw 404 when accessing another user's application")
    void testTenantIsolationFails() {
        when(applicationRepository.findByIdAndUserId(100L, 2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.getApplicationById(2L, 100L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Application not found");
    }
}
