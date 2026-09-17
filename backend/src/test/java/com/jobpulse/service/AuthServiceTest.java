package com.jobpulse.service;

import com.jobpulse.dto.request.LoginRequest;
import com.jobpulse.dto.request.RegisterRequest;
import com.jobpulse.dto.response.AuthResponse;
import com.jobpulse.entity.User;
import com.jobpulse.exception.BadRequestException;
import com.jobpulse.repository.UserRepository;
import com.jobpulse.security.JwtTokenProvider;
import com.jobpulse.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .password("hashed_password_123")
                .build();
    }

    @Test
    @DisplayName("Should successfully register a new user")
    void testRegisterSuccess() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .password("plain_password")
                .build();

        when(userRepository.existsByEmailIgnoreCase("jane.doe@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plain_password")).thenReturn("hashed_password_123");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(tokenProvider.generateToken(1L, "jane.doe@example.com")).thenReturn("mocked.jwt.token");

        AuthResponse response = authService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(response.getToken()).isEqualTo("mocked.jwt.token");
        verify(passwordEncoder).encode("plain_password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should reject registration when email already exists")
    void testRegisterDuplicateEmail() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .password("password123")
                .build();

        when(userRepository.existsByEmailIgnoreCase("jane.doe@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email is already registered");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should successfully login with valid credentials")
    void testLoginSuccess() {
        LoginRequest request = LoginRequest.builder()
                .email("jane.doe@example.com")
                .password("plain_password")
                .build();

        when(userRepository.findByEmailIgnoreCase("jane.doe@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("plain_password", "hashed_password_123")).thenReturn(true);
        when(tokenProvider.generateToken(1L, "jane.doe@example.com")).thenReturn("mocked.jwt.token");

        AuthResponse response = authService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mocked.jwt.token");
    }

    @Test
    @DisplayName("Should fail login when password does not match")
    void testLoginWrongPassword() {
        LoginRequest request = LoginRequest.builder()
                .email("jane.doe@example.com")
                .password("wrong_password")
                .build();

        when(userRepository.findByEmailIgnoreCase("jane.doe@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("wrong_password", "hashed_password_123")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid email or password");
    }
}
