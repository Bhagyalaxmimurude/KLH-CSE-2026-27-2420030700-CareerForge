
package com.careerforge.backend;

import com.careerforge.backend.service.CareerRecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CareerRecommendationServiceTests {

    private CareerRecommendationService service;

    @BeforeEach
    void setUp() {
        service = new CareerRecommendationService();
    }

    @Test
    void shouldRecommendAllFiveCareers() {
        List<Map<String, Object>> results =
                service.recommendCareers("java, sql, git");

        assertEquals(5, results.size());

        List<String> careers = results.stream()
                .map(result -> (String) result.get("career"))
                .toList();

        assertTrue(careers.contains("Software Engineer"));
        assertTrue(careers.contains("Backend Developer"));
        assertTrue(careers.contains("Cloud Engineer"));
        assertTrue(careers.contains("Data Engineer"));
        assertTrue(careers.contains("Java Developer"));
    }

    @Test
    void shouldCalculateSoftwareEngineerMatchPercentage() {
        List<Map<String, Object>> results =
                service.recommendCareers(
                        "java, sql, spring boot, git, github, data structures, algorithms"
                );

        Map<String, Object> softwareEngineer = findCareer(
                results, "Software Engineer"
        );

        assertEquals(100.0, softwareEngineer.get("matchPercentage"));
        assertEquals(7, softwareEngineer.get("matchedSkills"));
        assertEquals(7, softwareEngineer.get("totalRequiredSkills"));
    }

    @Test
    void shouldCalculateZeroMatchForEmptySkills() {
        List<Map<String, Object>> results =
                service.recommendCareers("");

        assertEquals(5, results.size());

        for (Map<String, Object> result : results) {
            assertEquals(0.0, result.get("matchPercentage"));
            assertEquals(0, result.get("matchedSkills"));
        }
    }

    @Test
    void shouldHandleNullSkills() {
        List<Map<String, Object>> results =
                service.recommendCareers(null);

        assertEquals(5, results.size());

        for (Map<String, Object> result : results) {
            assertEquals(0.0, result.get("matchPercentage"));
        }
    }

    @Test
    void shouldNormalizeSkillCaseAndWhitespace() {
        List<Map<String, Object>> results =
                service.recommendCareers(" JAVA , SQL , GIT ");

        Map<String, Object> javaDeveloper = findCareer(
                results, "Java Developer"
        );

        assertEquals(75.0, javaDeveloper.get("matchPercentage"));
        assertEquals(3, javaDeveloper.get("matchedSkills"));
    }

    @Test
    void shouldRecommendCloudEngineerForCloudSkills() {
        List<Map<String, Object>> results =
                service.recommendCareers(
                        "aws, docker, kubernetes, linux, git"
                );

        Map<String, Object> cloudEngineer = findCareer(
                results, "Cloud Engineer"
        );

        assertEquals(100.0, cloudEngineer.get("matchPercentage"));
        assertEquals(5, cloudEngineer.get("matchedSkills"));
    }

    @Test
    void shouldSortRecommendationsByMatchPercentage() {
        List<Map<String, Object>> results =
                service.recommendCareers(
                        "aws, docker, kubernetes, linux, git, java, sql"
                );

        for (int i = 0; i < results.size() - 1; i++) {
            double current = (Double) results.get(i).get("matchPercentage");
            double next = (Double) results.get(i + 1).get("matchPercentage");

            assertTrue(current >= next);
        }
    }

    @Test
    void shouldReturnZeroMatchForBlankSkills() {
        List<Map<String, Object>> results =
                service.recommendCareers("   ");

        assertEquals(5, results.size());

        for (Map<String, Object> result : results) {
            assertEquals(0.0, result.get("matchPercentage"));
        }
    }

    private Map<String, Object> findCareer(
            List<Map<String, Object>> results,
            String careerName) {

        return results.stream()
                .filter(result -> careerName.equals(result.get("career")))
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("Career not found: " + careerName));
    }
}