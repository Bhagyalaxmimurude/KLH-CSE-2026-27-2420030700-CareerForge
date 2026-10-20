
package com.careerforge.backend;

import com.careerforge.backend.dto.LoginRequest;
import com.careerforge.backend.dto.RegisterRequest;
import com.careerforge.backend.entity.User;
import com.careerforge.backend.repository.UserRepository;
import com.careerforge.backend.service.AuthService;
import com.careerforge.backend.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTests {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JwtService.class);

        authService = new AuthService(
                userRepository,
                passwordEncoder,
                jwtService
        );
    }

    @Test
    void shouldRegisterNewUser() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123"))
                .thenReturn("encoded-password");

        String result = authService.register(request);

        assertEquals("User registered successfully", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldRejectDuplicateEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(mock(User.class)));

        String result = authService.register(request);

        assertEquals("Email already registered", result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldLoginWithCorrectPassword() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        User user = mock(User.class);
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn("encoded-password");
        when(passwordEncoder.matches("password123", "encoded-password"))
                .thenReturn(true);
        when(user.getEmail()).thenReturn("test@example.com");
        when(jwtService.generateToken("test@example.com"))
                .thenReturn("test-jwt-token");

        String result = authService.login(request);

        assertEquals("test-jwt-token", result);
        verify(jwtService).generateToken("test@example.com");
    }

    @Test
    void shouldRejectUnknownEmail() {
        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        String result = authService.login(request);

        assertEquals("Invalid email or password", result);
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void shouldRejectIncorrectPassword() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrong-password");

        User user = mock(User.class);
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn("encoded-password");
        when(passwordEncoder.matches("wrong-password", "encoded-password"))
                .thenReturn(false);

        String result = authService.login(request);

        assertEquals("Invalid email or password", result);
        verify(jwtService, never()).generateToken(any());
    }
}