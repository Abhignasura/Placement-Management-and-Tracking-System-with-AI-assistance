package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rounds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roundId;

    @Column(name = "application_id")
    private Integer applicationId;

    private String roundName;   // Technical, HR, Manager etc.

    private String result;      // PASS or FAIL

    private Integer roundOrder; // 1,2,3...

    private LocalDateTime updatedAt = LocalDateTime.now();
}