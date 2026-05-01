package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "interview_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleId;

    @Column(name = "application_id")
    private Integer applicationId;

    private String roundName;

    private LocalDate interviewDate;

    private LocalTime interviewTime;

    private String mode; // ONLINE or OFFLINE

    private String locationOrLink; // Meeting link or venue
}