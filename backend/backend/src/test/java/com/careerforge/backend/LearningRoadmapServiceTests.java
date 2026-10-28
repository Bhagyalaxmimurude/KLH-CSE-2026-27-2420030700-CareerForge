
package com.careerforge.backend;

import com.careerforge.backend.service.LearningRoadmapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LearningRoadmapServiceTests {

    private LearningRoadmapService service;

    @BeforeEach
    void setUp() {
        service = new LearningRoadmapService();
    }

    @Test
    void shouldGenerateDockerRoadmap() {
        List<Map<String, Object>> roadmap =
                service.generateRoadmap(List.of("docker"));

        assertEquals(1, roadmap.size());
        assertEquals("Docker", roadmap.get(0).get("skill"));
        assertEquals("1 week", roadmap.get(0).get("duration"));

        List<?> topics = (List<?>) roadmap.get(0).get("topics");
        assertEquals(5, topics.size());
        assertTrue(topics.contains("Docker Compose"));
    }

    @Test
    void shouldGenerateRestApiRoadmap() {
        List<Map<String, Object>> roadmap =
                service.generateRoadmap(List.of("rest api"));

        assertEquals(1, roadmap.size());
        assertEquals("REST API", roadmap.get(0).get("skill"));
        assertEquals("1 week", roadmap.get(0).get("duration"));

        List<?> topics = (List<?>) roadmap.get(0).get("topics");
        assertEquals(5, topics.size());
        assertTrue(topics.contains("HTTP methods"));
    }

    @Test
    void shouldGenerateDefaultRoadmapForOtherSkills() {
        List<Map<String, Object>> roadmap =
                service.generateRoadmap(List.of("Java"));

        assertEquals(1, roadmap.size());
        assertEquals("Java", roadmap.get(0).get("skill"));
        assertEquals("1 week", roadmap.get(0).get("duration"));

        List<?> topics = (List<?>) roadmap.get(0).get("topics");
        assertEquals(4, topics.size());
        assertTrue(topics.contains("Fundamentals"));
    }

    @Test
    void shouldGenerateRoadmapsForMultipleSkills() {
        List<Map<String, Object>> roadmap =
                service.generateRoadmap(
                        List.of("docker", "rest api", "java")
                );

        assertEquals(3, roadmap.size());
        assertEquals("Docker", roadmap.get(0).get("skill"));
        assertEquals("REST API", roadmap.get(1).get("skill"));
        assertEquals("java", roadmap.get(2).get("skill"));
    }

    @Test
    void shouldReturnEmptyListForNoMissingSkills() {
        List<Map<String, Object>> roadmap =
                service.generateRoadmap(List.of());

        assertNotNull(roadmap);
        assertTrue(roadmap.isEmpty());
    }

    @Test
    void shouldHandleUppercaseDockerSkill() {
        List<Map<String, Object>> roadmap =
                service.generateRoadmap(List.of("DOCKER"));

        assertEquals("Docker", roadmap.get(0).get("skill"));
    }
}