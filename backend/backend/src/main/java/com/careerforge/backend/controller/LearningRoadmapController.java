package com.careerforge.backend.controller;

import com.careerforge.backend.entity.Resume;
import com.careerforge.backend.entity.User;
import com.careerforge.backend.repository.ResumeRepository;
import com.careerforge.backend.repository.UserRepository;
import com.careerforge.backend.service.LearningRoadmapService;
import com.careerforge.backend.service.SkillGapService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/learning-roadmap")
public class LearningRoadmapController {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final SkillGapService skillGapService;
    private final LearningRoadmapService learningRoadmapService;

    public LearningRoadmapController(
            ResumeRepository resumeRepository,
            UserRepository userRepository,
            SkillGapService skillGapService,
            LearningRoadmapService learningRoadmapService) {

        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.skillGapService = skillGapService;
        this.learningRoadmapService = learningRoadmapService;
    }

    @GetMapping
    public List<Map<String, Object>> getLearningRoadmap(
            Authentication authentication,
            @RequestParam(defaultValue = "Software Engineer") String targetRole) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<Resume> resumes = resumeRepository.findByUser(user);

        if (resumes.isEmpty()) {
            throw new RuntimeException("No resume found");
        }

        Resume latestResume = resumes.get(resumes.size() - 1);

        Map<String, Object> skillGap =
                skillGapService.analyzeSkillGap(
                        latestResume.getSkills(),
                        targetRole
                );

        List<String> missingSkills =
                (List<String>) skillGap.get("missingSkills");

        return learningRoadmapService.generateRoadmap(missingSkills);
    }
}