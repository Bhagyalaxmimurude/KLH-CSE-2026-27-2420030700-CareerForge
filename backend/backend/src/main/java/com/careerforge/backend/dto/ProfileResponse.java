package com.careerforge.backend.dto;

public class ProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String education;
    private String skills;
    private String careerGoal;
    private String experience;
    private String bio;

    public ProfileResponse() {
    }

    public ProfileResponse(
            Long id,
            String name,
            String email,
            String role,
            String education,
            String skills,
            String careerGoal,
            String experience,
            String bio) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.education = education;
        this.skills = skills;
        this.careerGoal = careerGoal;
        this.experience = experience;
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getEducation() {
        return education;
    }

    public String getSkills() {
        return skills;
    }

    public String getCareerGoal() {
        return careerGoal;
    }

    public String getExperience() {
        return experience;
    }

    public String getBio() {
        return bio;
    }
}