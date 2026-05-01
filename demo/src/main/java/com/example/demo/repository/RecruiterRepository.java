package com.example.demo.repository;

import com.example.demo.model.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<Recruiter, Integer> {

    Optional<Recruiter> findByUserId(Integer userId);
    List<Recruiter> findAll();
}