package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStatusDTO {

    private Integer applicationId;
    private String companyName;
    private String jobTitle;
    private String status;

}