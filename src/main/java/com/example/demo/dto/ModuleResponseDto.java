package com.example.demo.dto;

import com.example.demo.enums.ContentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor  // Generates a constructor with all fields as arguments
@NoArgsConstructor  
public class ModuleResponseDto {

    private Long id;
    private String courseName;
    private String title;
    private String description;
    private ContentType contentType;
    private String contentUrl;
    private boolean published;
    private Long addedById;

}