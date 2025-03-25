package com.example.demo.dto;

import com.example.demo.enums.ContentType;

import lombok.Data;

@Data
public class ModuleRequestDto {
    private String courseName;
    private String title;
    private String description;
    private ContentType contentType;
    private String contentUrl;
}