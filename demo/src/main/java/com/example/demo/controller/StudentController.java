package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Student;
import com.example.demo.model.User;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;

@RestController
public class StudentController {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentController(StudentRepository studentRepository,
                             UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/api/student/profile")
    public Student getProfile(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        return studentRepository.findByUserId(user.getUserId())
                .orElseThrow();
    }
}