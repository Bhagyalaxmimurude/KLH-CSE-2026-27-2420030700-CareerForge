
package com.careerforge.backend;

import com.careerforge.backend.service.SkillGapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SkillGapServiceTests {

    private SkillGapService skillGapService;

    @BeforeEach
    void setUp() {
        skillGapService = new SkillGapService();
    }

    @Test
    void shouldIdentifyAllRequiredSkillsAsMatched() {
        Map<String, Object> result = skillGapService.analyzeSkillGap(
                "java, sql, spring boot, git, github, docker, rest api, data structures, algorithms",
                "Software Engineer"
        );

        assertEquals(100.0, result.get("skillMatchPercentage"));
        assertEquals(0.0, result.get("skillGapPercentage"));
        assertEquals(9, ((List<?>) result.get("matchedSkills")).size());
        assertTrue(((List<?>) result.get("missingSkills")).isEmpty());
    }

    @Test
    void shouldIdentifyMissingSkills() {
        Map<String, Object> result = skillGapService.analyzeSkillGap(
                "java, sql",
                "Software Engineer"
        );

        assertEquals(22.22, result.get("skillMatchPercentage"));
        assertEquals(77.78, result.get("skillGapPercentage"));
        assertEquals(2, ((List<?>) result.get("matchedSkills")).size());
        assertEquals(7, ((List<?>) result.get("missingSkills")).size());
    }

    @Test
    void shouldHandleNullUserSkills() {
        Map<String, Object> result = skillGapService.analyzeSkillGap(
                null,
                "Software Engineer"
        );

        assertEquals(0.0, result.get("skillMatchPercentage"));
        assertEquals(100.0, result.get("skillGapPercentage"));
        assertTrue(((List<?>) result.get("matchedSkills")).isEmpty());
        assertEquals(9, ((List<?>) result.get("missingSkills")).size());
    }

    @Test
    void shouldHandleBlankUserSkills() {
        Map<String, Object> result = skillGapService.analyzeSkillGap(
                "   ",
                "Software Engineer"
        );

        assertEquals(0.0, result.get("skillMatchPercentage"));
        assertTrue(((List<?>) result.get("currentSkills")).isEmpty());
    }

    @Test
    void shouldNormalizeSkillCaseAndWhitespace() {
        Map<String, Object> result = skillGapService.analyzeSkillGap(
                " JAVA , SQL , Spring Boot ",
                "Software Engineer"
        );

        assertEquals(3, ((List<?>) result.get("matchedSkills")).size());
        assertEquals(33.33, result.get("skillMatchPercentage"));
    }

    @Test
    void shouldPreserveRequestedTargetRole() {
        Map<String, Object> result = skillGapService.analyzeSkillGap(
                "java",
                "Backend Developer"
        );

        assertEquals("Backend Developer", result.get("targetRole"));
    }
}