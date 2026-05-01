package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobViewDTO {

    private Integer jobId;
    private String title;
    private String companyName;
    private Double minCgpa;
    private String deadline;

}