package com.careerforge.backend.controller;

import com.careerforge.backend.entity.Resume;
import com.careerforge.backend.entity.User;
import com.careerforge.backend.repository.ResumeRepository;
import com.careerforge.backend.repository.UserRepository;
import com.careerforge.backend.service.CareerRecommendationService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/career-recommendations")
public class CareerRecommendationController {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final CareerRecommendationService careerRecommendationService;

    public CareerRecommendationController(
            ResumeRepository resumeRepository,
            UserRepository userRepository,
            CareerRecommendationService careerRecommendationService) {

        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.careerRecommendationService = careerRecommendationService;
    }

    @GetMapping
    public List<Map<String, Object>> getCareerRecommendations(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<Resume> resumes = resumeRepository.findByUser(user);

        if (resumes.isEmpty()) {
            throw new RuntimeException("No resume found");
        }

        Resume latestResume = resumes.get(resumes.size() - 1);

        return careerRecommendationService.recommendCareers(
                latestResume.getSkills()
        );
    }
}