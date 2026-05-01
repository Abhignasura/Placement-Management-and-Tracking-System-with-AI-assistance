package com.example.demo.repository;

import com.example.demo.model.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Integer> {

    List<InterviewSchedule> findByApplicationId(Integer applicationId);
    List<InterviewSchedule> findByApplicationIdIn(List<Integer> applicationIds);
}