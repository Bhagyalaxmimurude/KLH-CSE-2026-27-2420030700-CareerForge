package com.careerforge.backend.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SkillGapService {

    public Map<String, Object> analyzeSkillGap(
            String userSkills,
            String targetRole) {

        Map<String, Object> result = new HashMap<>();

        // Skills required for Software Engineer
        List<String> requiredSkills = Arrays.asList(
                "java",
                "sql",
                "spring boot",
                "git",
                "github",
                "docker",
                "rest api",
                "data structures",
                "algorithms"
        );

        List<String> currentSkills = new ArrayList<>();

        if (userSkills != null && !userSkills.isBlank()) {

            String[] skills = userSkills.toLowerCase().split(",");

            for (String skill : skills) {
                currentSkills.add(skill.trim());
            }
        }

        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String required : requiredSkills) {

            if (currentSkills.contains(required)) {
                matchedSkills.add(required);
            } else {
                missingSkills.add(required);
            }
        }

        double skillMatchPercentage =
        Math.round(((double) matchedSkills.size() / requiredSkills.size()) * 10000.0) / 100.0;

double skillGapPercentage =
        Math.round((100 - skillMatchPercentage) * 100.0) / 100.0;
result.put("targetRole", targetRole);
result.put("currentSkills", currentSkills);
result.put("requiredSkills", requiredSkills);
result.put("matchedSkills", matchedSkills);
result.put("missingSkills", missingSkills);
result.put("skillMatchPercentage", skillMatchPercentage);
result.put("skillGapPercentage", skillGapPercentage);

        return result;
    }
}