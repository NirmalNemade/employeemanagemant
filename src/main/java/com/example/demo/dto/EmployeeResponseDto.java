package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeeResponseDto {
    
    private Long id;
    private String fullName;
    private String department;
    private String contactNumber;
    private String email;
    private LocalDate dateOfJoining;
}
    