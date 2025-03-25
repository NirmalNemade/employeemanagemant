package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.EmployeeRequestDto;
import com.example.demo.dto.EmployeeResponseDto;
import com.example.demo.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @Secured({ "ROLE_HR", "ROLE_ADMIN" })
    public ResponseEntity<EmployeeResponseDto> addEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
        return new ResponseEntity<>(employeeService.addEmployee(employeeRequestDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Secured({ "ROLE_HR", "ROLE_ADMIN" })
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    @Secured({ "ROLE_HR", "ROLE_ADMIN" })
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PutMapping("/{id}")
    @Secured({ "ROLE_HR", "ROLE_ADMIN" })
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequestDto employeeRequestDto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeRequestDto));
    }

    @DeleteMapping("/{id}")
    @Secured({ "ROLE_HR", "ROLE_ADMIN" })
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    @GetMapping("/search")
    @Secured({ "ROLE_HR", "ROLE_ADMIN" })
    public ResponseEntity<List<EmployeeResponseDto>> searchEmployees(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.searchEmployeesByName(name));
    }

    @GetMapping("/filter")
    @Secured({ "ROLE_HR", "ROLE_ADMIN" })
    public ResponseEntity<List<EmployeeResponseDto>> filterEmployees(@RequestParam String department) {
        return ResponseEntity.ok(employeeService.filterEmployeesByDepartment(department));
    }
}
