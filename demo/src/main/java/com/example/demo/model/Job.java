package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    @Column(name = "recruiter_id")
    private Integer recruiterId;

    private String title;

    private String description;

    @Column(name = "min_cgpa")
    private Double minCgpa;

    @Column(name = "application_deadline")
    private LocalDate applicationDeadline;
    
    private Boolean approved = false;
}