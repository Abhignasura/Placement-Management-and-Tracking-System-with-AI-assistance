package com.example.demo.repository;

import com.example.demo.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer> {
	List<Job> findByApprovedTrueAndApplicationDeadlineAfter(LocalDate date);
	long countByApprovedTrueAndApplicationDeadlineAfter(LocalDate date);
	List<Job> findByApprovedFalse();
}
