package com.careerforge.backend.repository;

import com.careerforge.backend.entity.Resume;
import com.careerforge.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByUser(User user);
}