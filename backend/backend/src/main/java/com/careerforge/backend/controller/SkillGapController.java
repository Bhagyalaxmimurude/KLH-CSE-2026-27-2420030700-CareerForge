package com.careerforge.backend.controller;

import com.careerforge.backend.entity.Resume;
import com.careerforge.backend.entity.User;
import com.careerforge.backend.repository.ResumeRepository;
import com.careerforge.backend.repository.UserRepository;
import com.careerforge.backend.service.SkillGapService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/skill-gap")
public class SkillGapController {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final SkillGapService skillGapService;

    public SkillGapController(
            ResumeRepository resumeRepository,
            UserRepository userRepository,
            SkillGapService skillGapService) {

        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.skillGapService = skillGapService;
    }

    @GetMapping
    public Map<String, Object> analyzeSkillGap(
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

        return skillGapService.analyzeSkillGap(
                latestResume.getSkills(),
                targetRole
        );
    }
}