package com.example.demo.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String role; // Add this to accept the role from frontend
}
