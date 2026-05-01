package com.example.demo.dto;

import lombok.Data;

@Data
public class ApplicantDTO {

    private Integer applicationId;

    private String studentName;

    private String branch;

    private Double cgpa;

    private String jobTitle;

    private String companyName;

    private String status;
    
    private String rollNo;

}