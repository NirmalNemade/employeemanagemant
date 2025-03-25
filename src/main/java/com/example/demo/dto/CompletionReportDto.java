package com.example.demo.dto;

import lombok.Data;

@Data
public class CompletionReportDto {
    private Long moduleId;
    private String moduleTitle;
    private String courseName;
    private int completedCount;
    private double completionPercentage;
}