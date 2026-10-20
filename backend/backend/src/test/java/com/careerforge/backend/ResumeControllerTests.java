
package com.careerforge.backend;

import com.careerforge.backend.controller.ResumeController;
import com.careerforge.backend.entity.Resume;
import com.careerforge.backend.entity.User;
import com.careerforge.backend.repository.ResumeRepository;
import com.careerforge.backend.repository.UserRepository;
import com.careerforge.backend.service.ResumeAnalysisService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ResumeControllerTests {

    private ResumeRepository resumeRepository;
    private UserRepository userRepository;
    private ResumeAnalysisService resumeAnalysisService;
    private ResumeController controller;
    private Authentication authentication;
    private User user;

    @BeforeEach
    void setUp() {
        resumeRepository = mock(ResumeRepository.class);
        userRepository = mock(UserRepository.class);
        resumeAnalysisService = mock(ResumeAnalysisService.class);
        authentication = mock(Authentication.class);
        user = mock(User.class);

        controller = new ResumeController(
                resumeRepository,
                userRepository,
                resumeAnalysisService
        );

        when(authentication.getName()).thenReturn("test@example.com");
    }

    @Test
    void shouldRejectEmptyFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "resume.pdf", "application/pdf", new byte[0]
        );

        ResponseEntity<String> response =
                controller.uploadResume(file, authentication);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Please upload a resume", response.getBody());
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldRejectNonPdfFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "resume.docx", "application/octet-stream",
                "sample content".getBytes()
        );

        ResponseEntity<String> response =
                controller.uploadResume(file, authentication);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Only PDF files are allowed", response.getBody());
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldUploadAndAnalyzeValidPdf() throws Exception {
        byte[] pdfBytes = createPdf(
                "Java Python SQL B.Tech Internship"
        );

        MockMultipartFile file = new MockMultipartFile(
                "file", "resume.pdf", "application/pdf", pdfBytes
        );

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(resumeAnalysisService.extractSkills(any(String.class)))
                .thenReturn("java, python, sql");
        when(resumeAnalysisService.extractEducation(any(String.class)))
                .thenReturn("B.Tech");
        when(resumeAnalysisService.extractExperience(any(String.class)))
                .thenReturn("Internship experience");

        ResponseEntity<String> response =
                controller.uploadResume(file, authentication);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(
                "Resume uploaded and text extracted successfully",
                response.getBody()
        );

        verify(resumeRepository).save(any(Resume.class));
        verify(resumeAnalysisService).extractSkills(any(String.class));
        verify(resumeAnalysisService).extractEducation(any(String.class));
        verify(resumeAnalysisService).extractExperience(any(String.class));
    }

    @Test
    void shouldReturnServerErrorWhenUserDoesNotExist() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "resume.pdf", "application/pdf",
                "not a real PDF".getBytes()
        );

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        ResponseEntity<String> response =
                controller.uploadResume(file, authentication);

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().contains("User not found"));
        verify(resumeRepository, never()).save(any(Resume.class));
    }

    @Test
    void shouldReturnServerErrorForInvalidPdf() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "resume.pdf", "application/pdf",
                "invalid pdf content".getBytes()
        );

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        ResponseEntity<String> response =
                controller.uploadResume(file, authentication);

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().startsWith("Error processing resume:"));
        verify(resumeRepository, never()).save(any(Resume.class));
    }

    private byte[] createPdf(String text) throws Exception {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream stream =
                         new PDPageContentStream(document, page)) {
                stream.beginText();
                stream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                stream.newLineAtOffset(50, 700);
                stream.showText(text);
                stream.endText();
            }

            document.save(output);
            return output.toByteArray();
        }
    }
}