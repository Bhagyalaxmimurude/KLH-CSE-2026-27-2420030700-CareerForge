package com.careerforge.backend.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeAnalysisService {

    public String extractSkills(String text) {

        String lowerText = text.toLowerCase();

        List<String> skills = new ArrayList<>();

        String[] skillList = {
                "java",
                "python",
                "c++",
                "javascript",
                "typescript",
                "react",
                "spring boot",
                "sql",
                "mysql",
                "postgresql",
                "aws",
                "azure",
                "docker",
                "kubernetes",
                "git",
                "github",
                "machine learning",
                "data structures",
                "algorithms",
                "pandas",
                "numpy",
                "scikit-learn"
        };

        for (String skill : skillList) {

            if (lowerText.contains(skill.toLowerCase())) {
                skills.add(skill);
            }
        }

        return String.join(", ", skills);
    }

    public String extractEducation(String text) {

    String lowerText = text.toLowerCase();

    if (lowerText.contains("b.tech") ||
        lowerText.contains("bachelor of technology")) {
        return "B.Tech";
    }

    if (lowerText.contains("b.tech") ||
    lowerText.contains("bachelor of technology") ||
    lowerText.contains("bachelors of technology")) {
    return "B.Tech";
}

    if (lowerText.contains("m.tech") ||
        lowerText.contains("master of technology")) {
        return "M.Tech";
    }

    if (lowerText.contains("mca")) {
        return "MCA";
    }

    if (lowerText.contains("bca")) {
        return "BCA";
    }

    return "Not detected";
}

public String extractExperience(String text) {

    String lowerText = text.toLowerCase();

    if (lowerText.contains("internship") ||
        lowerText.contains("intern")) {
        return "Internship experience";
    }

    if (lowerText.contains("experience") ||
        lowerText.contains("work experience")) {
        return "Work experience mentioned";
    }

    if (lowerText.contains("fresher") ||
        lowerText.contains("student")) {
        return "Fresher / Student";
    }

    return "Not detected";
}
}