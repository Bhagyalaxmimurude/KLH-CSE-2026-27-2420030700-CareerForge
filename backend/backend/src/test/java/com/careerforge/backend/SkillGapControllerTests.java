
package com.careerforge.backend;

import com.careerforge.backend.controller.SkillGapController;
import com.careerforge.backend.entity.Resume;
import com.careerforge.backend.entity.User;
import com.careerforge.backend.repository.ResumeRepository;
import com.careerforge.backend.repository.UserRepository;
import com.careerforge.backend.service.SkillGapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkillGapControllerTests {

    private ResumeRepository resumeRepository;
    private UserRepository userRepository;
    private SkillGapController controller;
    private Authentication authentication;
    private User user;

    @BeforeEach
    void setUp() {
        resumeRepository = mock(ResumeRepository.class);
        userRepository = mock(UserRepository.class);
        authentication = mock(Authentication.class);
        user = mock(User.class);

        controller = new SkillGapController(
                resumeRepository,
                userRepository,
                new SkillGapService()
        );

        when(authentication.getName()).thenReturn("test@example.com");
    }

    @Test
    void shouldAnalyzeSkillsUsingLatestResume() {
        Resume oldResume = mock(Resume.class);
        Resume latestResume = mock(Resume.class);

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        when(resumeRepository.findByUser(user))
                .thenReturn(List.of(oldResume, latestResume));

        when(oldResume.getSkills()).thenReturn("java");
        when(latestResume.getSkills()).thenReturn("java, sql");

        Map<String, Object> result =
                controller.analyzeSkillGap(authentication, "Software Engineer");

        assertEquals("Software Engineer", result.get("targetRole"));
        assertEquals(22.22, result.get("skillMatchPercentage"));
        assertEquals(2, ((List<?>) result.get("matchedSkills")).size());

        verify(latestResume).getSkills();
    }

    @Test
    void shouldThrowErrorWhenUserDoesNotExist() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                controller.analyzeSkillGap(authentication, "Software Engineer")
        );

        verify(resumeRepository, never()).findByUser(any());
    }

    @Test
    void shouldThrowErrorWhenUserHasNoResume() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        when(resumeRepository.findByUser(user))
                .thenReturn(List.of());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> controller.analyzeSkillGap(
                        authentication, "Software Engineer")
        );

        assertEquals("No resume found", exception.getMessage());
    }
}