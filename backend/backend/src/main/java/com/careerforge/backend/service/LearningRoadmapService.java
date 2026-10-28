package com.careerforge.backend.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LearningRoadmapService {

    public List<Map<String, Object>> generateRoadmap(
            List<String> missingSkills) {

        List<Map<String, Object>> roadmap = new ArrayList<>();

        for (String skill : missingSkills) {

            Map<String, Object> topic = new HashMap<>();

            switch (skill.toLowerCase()) {

                case "docker":
                    topic.put("skill", "Docker");
                    topic.put("duration", "1 week");
                    topic.put("topics", Arrays.asList(
                            "Docker basics",
                            "Images and containers",
                            "Dockerfile",
                            "Docker Compose",
                            "Containerizing Spring Boot applications"
                    ));
                    break;

                case "rest api":
                    topic.put("skill", "REST API");
                    topic.put("duration", "1 week");
                    topic.put("topics", Arrays.asList(
                            "REST API basics",
                            "HTTP methods",
                            "GET, POST, PUT and DELETE",
                            "Request and response handling",
                            "Building REST APIs with Spring Boot"
                    ));
                    break;

                default:
                    topic.put("skill", skill);
                    topic.put("duration", "1 week");
                    topic.put("topics", Arrays.asList(
                            "Fundamentals",
                            "Core concepts",
                            "Practical implementation",
                            "Mini project"
                    ));
            }

            roadmap.add(topic);
        }

        return roadmap;
    }
}