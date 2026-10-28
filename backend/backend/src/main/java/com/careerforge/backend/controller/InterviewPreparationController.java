package com.careerforge.backend.controller;

import com.careerforge.backend.service.InterviewPreparationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interview")
public class InterviewPreparationController {

    private final InterviewPreparationService interviewPreparationService;

    public InterviewPreparationController(
            InterviewPreparationService interviewPreparationService) {
        this.interviewPreparationService = interviewPreparationService;
    }

    @GetMapping("/questions")
    public List<Map<String, Object>> getInterviewQuestions(
            @RequestParam(defaultValue = "Software Engineer") String role) {

        return interviewPreparationService.generateQuestions(role);
    }
}
