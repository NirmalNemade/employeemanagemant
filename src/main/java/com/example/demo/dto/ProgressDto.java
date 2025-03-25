package com.example.demo.dto;

import lombok.Data;

@Data
public class ProgressDto {
    private int totalModules;
    private int completedModules;
    private double progressPercentage;
}