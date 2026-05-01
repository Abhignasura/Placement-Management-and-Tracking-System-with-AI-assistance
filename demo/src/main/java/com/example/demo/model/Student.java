package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    @Column(name = "user_id")
    private Integer userId;

    private String name;

    private Double cgpa;

    private String branch;
    
    @Column(name = "roll_no")
    private String rollNo;
}