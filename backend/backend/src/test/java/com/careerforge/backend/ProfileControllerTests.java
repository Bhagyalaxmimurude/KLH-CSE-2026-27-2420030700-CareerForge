
package com.careerforge.backend;

import com.careerforge.backend.controller.ProfileController;
import com.careerforge.backend.dto.ProfileResponse;
import com.careerforge.backend.dto.ProfileUpdateRequest;
import com.careerforge.backend.entity.User;
import com.careerforge.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileControllerTests {

    private UserRepository userRepository;
    private ProfileController controller;
    private Authentication authentication;
    private User user;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        authentication = mock(Authentication.class);
        controller = new ProfileController(userRepository);

        user = new User();
        user.setId(1L);
        user.setName("Vaishnavi");
        user.setEmail("test@example.com");
        user.setRole("USER");
        user.setEducation("B.Tech CSE");
        user.setSkills("Java, SQL");
        user.setCareerGoal("Software Engineer");
        user.setExperience("Student");
        user.setBio("Computer Science student");

        when(authentication.getName()).thenReturn("test@example.com");
    }

    @Test
    void shouldGetProfileSuccessfully() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        ProfileResponse response = controller.getProfile(authentication);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Vaishnavi", response.getName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("USER", response.getRole());
        assertEquals("B.Tech CSE", response.getEducation());
        assertEquals("Java, SQL", response.getSkills());
        assertEquals("Software Engineer", response.getCareerGoal());
        assertEquals("Student", response.getExperience());
        assertEquals("Computer Science student", response.getBio());

        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void shouldThrowExceptionWhenGettingProfileForUnknownUser() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> controller.getProfile(authentication)
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldUpdateProfileSuccessfully() {
        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setName("Vaishnavi Updated");
        request.setEducation("B.Tech Computer Science");
        request.setSkills("Java, Spring Boot, PostgreSQL");
        request.setCareerGoal("Backend Developer");
        request.setExperience("Intern");
        request.setBio("Aspiring software engineer");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        ProfileResponse response =
                controller.updateProfile(authentication, request);

        assertNotNull(response);
        assertEquals("Vaishnavi Updated", response.getName());
        assertEquals("B.Tech Computer Science", response.getEducation());
        assertEquals("Java, Spring Boot, PostgreSQL", response.getSkills());
        assertEquals("Backend Developer", response.getCareerGoal());
        assertEquals("Intern", response.getExperience());
        assertEquals("Aspiring software engineer", response.getBio());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("USER", response.getRole());

        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingProfileForUnknownUser() {
        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setName("Updated Name");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> controller.updateProfile(authentication, request)
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldAllowUpdatingProfileFieldsToNull() {
        ProfileUpdateRequest request = new ProfileUpdateRequest();

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        ProfileResponse response =
                controller.updateProfile(authentication, request);

        assertNotNull(response);
        assertNull(response.getName());
        assertNull(response.getEducation());
        assertNull(response.getSkills());
        assertNull(response.getCareerGoal());
        assertNull(response.getExperience());
        assertNull(response.getBio());

        verify(userRepository).save(user);
    }
}