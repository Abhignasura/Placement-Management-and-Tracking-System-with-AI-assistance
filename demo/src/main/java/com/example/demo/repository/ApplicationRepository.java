package com.example.demo.repository;

import com.example.demo.model.Application;
import com.example.demo.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    Optional<Application> findByStudentIdAndJobId(Integer studentId, Integer jobId);
    List<Application> findByStatus(String status);
    List<Application> findByStudentId(Integer studentId);
    List<Application> findByJobId(Integer jobId);
    long countByJobIdAndStatus(Integer jobId, String status);
    List<Application> findByJobIdAndStatus(Integer jobId, String status);
    long countByJobId(Integer jobId);
    long countByStatus(String status);
	long countByJobIdIn(List<Integer> jobIds);
	long countByJobIdInAndStatus(List<Integer> jobIds, String string);
}