package com.careerforge.backend.controller;
import com.careerforge.backend.service.ResumeAnalysisService;
import com.careerforge.backend.entity.Resume;
import com.careerforge.backend.entity.User;
import com.careerforge.backend.repository.ResumeRepository;
import com.careerforge.backend.repository.UserRepository;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {
    
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final ResumeAnalysisService resumeAnalysisService;

    public ResumeController(
        ResumeRepository resumeRepository,
        UserRepository userRepository,
        ResumeAnalysisService resumeAnalysisService) {

    this.resumeRepository = resumeRepository;
    this.userRepository = userRepository;
    this.resumeAnalysisService = resumeAnalysisService;
}

    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        try {

            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Please upload a resume");
            }

            if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                return ResponseEntity.badRequest()
                        .body("Only PDF files are allowed");
            }

            String email = authentication.getName();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("User not found"));

            PDDocument document =
                    Loader.loadPDF(file.getBytes());

            PDFTextStripper stripper =
                    new PDFTextStripper();

            String extractedText =
                    stripper.getText(document);

            document.close();

            Resume resume = new Resume();

            resume.setFileName(file.getOriginalFilename());
            resume.setExtractedText(extractedText);
String skills = resumeAnalysisService.extractSkills(extractedText);
String education = resumeAnalysisService.extractEducation(extractedText);
String experience = resumeAnalysisService.extractExperience(extractedText);

resume.setSkills(skills);
resume.setEducation(education);
resume.setExperience(experience);

resume.setUser(user);

            resumeRepository.save(resume);

            return ResponseEntity.ok(
                    "Resume uploaded and text extracted successfully");

        } catch (Exception e) {

            return ResponseEntity.internalServerError()
                    .body("Error processing resume: " + e.getMessage());
        }
    }
}