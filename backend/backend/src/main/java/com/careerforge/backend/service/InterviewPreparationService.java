
package com.careerforge.backend.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InterviewPreparationService {

    public List<Map<String, Object>> generateQuestions(String role) {

        List<Map<String, Object>> questions = new ArrayList<>();

        questions.add(createQuestion(
                "Java",
                "What is the difference between an interface and an abstract class in Java?",
                "Explain the main differences and when each should be used."
        ));

        questions.add(createQuestion(
                "Spring Boot",
                "What is Spring Boot and what are its advantages?",
                "Explain auto-configuration, starter dependencies, and embedded servers."
        ));

        questions.add(createQuestion(
                "REST API",
                "What is a REST API?",
                "Explain HTTP methods such as GET, POST, PUT, and DELETE."
        ));

        questions.add(createQuestion(
                "SQL",
                "What is the difference between INNER JOIN and LEFT JOIN?",
                "Explain how each join works and give a simple example."
        ));

        questions.add(createQuestion(
                "Docker",
                "What is Docker and why is it used?",
                "Explain containers, images, and the benefits of containerization."
        ));

        questions.add(createQuestion(
                "Projects",
                "Explain one of your projects and your contribution to it.",
                "Describe the problem, technologies used, your role, and the result."
        ));

        return questions;
    }

    private Map<String, Object> createQuestion(
            String topic,
            String question,
            String expectedAnswer) {

        Map<String, Object> item = new LinkedHashMap<>();

        item.put("topic", topic);
        item.put("question", question);
        item.put("expectedAnswer", expectedAnswer);

        return item;
    }
}
