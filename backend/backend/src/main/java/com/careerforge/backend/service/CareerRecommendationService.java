package com.careerforge.backend.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CareerRecommendationService {

    public List<Map<String, Object>> recommendCareers(
            String userSkills) {

        List<Map<String, Object>> recommendations = new ArrayList<>();

        List<String> currentSkills = new ArrayList<>();

        if (userSkills != null && !userSkills.isBlank()) {

            String[] skills = userSkills.toLowerCase().split(",");

            for (String skill : skills) {
                currentSkills.add(skill.trim());
            }
        }

        // Software Engineer
        addCareer(
                recommendations,
                currentSkills,
                "Software Engineer",
                Arrays.asList(
                        "java",
                        "sql",
                        "spring boot",
                        "git",
                        "github",
                        "data structures",
                        "algorithms"
                )
        );

        // Backend Developer
        addCareer(
                recommendations,
                currentSkills,
                "Backend Developer",
                Arrays.asList(
                        "java",
                        "spring boot",
                        "sql",
                        "rest api",
                        "git"
                )
        );

        // Cloud Engineer
        addCareer(
                recommendations,
                currentSkills,
                "Cloud Engineer",
                Arrays.asList(
                        "aws",
                        "docker",
                        "kubernetes",
                        "linux",
                        "git"
                )
        );

        // Data Engineer
        addCareer(
                recommendations,
                currentSkills,
                "Data Engineer",
                Arrays.asList(
                        "python",
                        "sql",
                        "pandas",
                        "numpy",
                        "git"
                )
        );

        // Java Developer
        addCareer(
                recommendations,
                currentSkills,
                "Java Developer",
                Arrays.asList(
                        "java",
                        "spring boot",
                        "sql",
                        "git"
                )
        );

        recommendations.sort((a, b) ->
        Double.compare(
                (Double) b.get("matchPercentage"),
                (Double) a.get("matchPercentage")
        )
);

return recommendations;
    }

    private void addCareer(
            List<Map<String, Object>> recommendations,
            List<String> currentSkills,
            String career,
            List<String> requiredSkills) {

        int matchedSkills = 0;

        for (String requiredSkill : requiredSkills) {

            if (currentSkills.contains(requiredSkill)) {
                matchedSkills++;
            }
        }

        double matchPercentage =
                Math.round(
                        ((double) matchedSkills / requiredSkills.size()) * 10000.0
                ) / 100.0;

        Map<String, Object> result = new HashMap<>();

        result.put("career", career);
        result.put("matchPercentage", matchPercentage);
        result.put("matchedSkills", matchedSkills);
        result.put("totalRequiredSkills", requiredSkills.size());

        recommendations.add(result);
    }
}