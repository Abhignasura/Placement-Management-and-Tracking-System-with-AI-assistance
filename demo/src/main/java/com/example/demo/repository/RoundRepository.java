package com.example.demo.repository;

import com.example.demo.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoundRepository extends JpaRepository<Round, Integer> {

    List<Round> findByApplicationId(Integer applicationId);
    long countByRoundNameAndResult(String roundName, String result);
}