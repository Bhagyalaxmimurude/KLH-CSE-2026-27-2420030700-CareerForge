package com.careerforge.backend.controller;
import com.careerforge.backend.dto.ProfileUpdateRequest;
import com.careerforge.backend.dto.ProfileResponse;
import com.careerforge.backend.entity.User;
import com.careerforge.backend.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/profile")
    public ProfileResponse getProfile(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getEducation(),
                user.getSkills(),
                user.getCareerGoal(),
                user.getExperience(),
                user.getBio()
        );
    }

    @PutMapping("/api/profile")
public ProfileResponse updateProfile(
        Authentication authentication,
        @RequestBody ProfileUpdateRequest request) {

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    user.setName(request.getName());
    user.setEducation(request.getEducation());
    user.setSkills(request.getSkills());
    user.setCareerGoal(request.getCareerGoal());
    user.setExperience(request.getExperience());
    user.setBio(request.getBio());

    userRepository.save(user);

    return new ProfileResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            user.getEducation(),
            user.getSkills(),
            user.getCareerGoal(),
            user.getExperience(),
            user.getBio()
    );
}
}