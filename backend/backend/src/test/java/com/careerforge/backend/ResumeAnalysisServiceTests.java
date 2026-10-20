
package com.careerforge.backend;

import com.careerforge.backend.service.ResumeAnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResumeAnalysisServiceTests {

    private ResumeAnalysisService service;

    @BeforeEach
    void setUp() {
        service = new ResumeAnalysisService();
    }

    @Test
    void shouldExtractMultipleSkills() {
        String result = service.extractSkills(
                "Java, Python, Spring Boot, SQL, Docker and GitHub"
        );

        assertTrue(result.contains("java"));
        assertTrue(result.contains("python"));
        assertTrue(result.contains("spring boot"));
        assertTrue(result.contains("sql"));
        assertTrue(result.contains("docker"));
        assertTrue(result.contains("github"));
    }

    @Test
    void shouldReturnEmptyStringWhenNoSkillsAreFound() {
        assertEquals(
                "",
                service.extractSkills("Communication and teamwork")
        );
    }

    @Test
    void shouldExtractSkillsWithoutCaseSensitivity() {
        String result = service.extractSkills("JAVA, PYTHON, POSTGRESQL");

        assertTrue(result.contains("java"));
        assertTrue(result.contains("python"));
        assertTrue(result.contains("postgresql"));
    }

    @Test
    void shouldDetectBTechEducation() {
        assertEquals("B.Tech", service.extractEducation("B.Tech CSE"));
        assertEquals(
                "B.Tech",
                service.extractEducation("Bachelor of Technology")
        );
    }

    @Test
    void shouldDetectMTechEducation() {
        assertEquals("M.Tech", service.extractEducation("M.Tech"));
        assertEquals(
                "M.Tech",
                service.extractEducation("Master of Technology")
        );
    }

    @Test
    void shouldDetectMcaEducation() {
        assertEquals("MCA", service.extractEducation("MCA graduate"));
    }

    @Test
    void shouldDetectBcaEducation() {
        assertEquals("BCA", service.extractEducation("BCA graduate"));
    }

    @Test
    void shouldReturnNotDetectedForUnknownEducation() {
        assertEquals(
                "Not detected",
                service.extractEducation("Computer Science student")
        );
    }

    @Test
    void shouldDetectInternshipExperience() {
        assertEquals(
                "Internship experience",
                service.extractExperience("Completed an internship")
        );
        assertEquals(
                "Internship experience",
                service.extractExperience("Worked as an intern")
        );
    }

    @Test
    void shouldDetectWorkExperience() {
        assertEquals(
                "Work experience mentioned",
                service.extractExperience("Previous work experience")
        );
        assertEquals(
                "Work experience mentioned",
                service.extractExperience("Experience in software development")
        );
    }

    @Test
    void shouldDetectFresherOrStudent() {
        assertEquals(
                "Fresher / Student",
                service.extractExperience("I am a fresher")
        );
        assertEquals(
                "Fresher / Student",
                service.extractExperience("Computer science student")
        );
    }

    @Test
    void shouldReturnNotDetectedForUnknownExperience() {
        assertEquals(
                "Not detected",
                service.extractExperience("Interested in software development")
        );
    }
@Test
void shouldDetectBachelorsOfTechnologyEducation() {
    assertEquals(
            "B.Tech",
            service.extractEducation("Bachelors of Technology")
    );
}
}