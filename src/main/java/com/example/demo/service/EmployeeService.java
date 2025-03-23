package com.example.demo.service;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.EmployeeRequestDto;
import com.example.demo.dto.EmployeeResponseDto;
import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto) {
        Employee employee = modelMapper.map(employeeRequestDto, Employee.class);
        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeResponseDto.class);
    }

    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(emp -> modelMapper.map(emp, EmployeeResponseDto.class))
                .collect(Collectors.toList());
    }

    public EmployeeResponseDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        return modelMapper.map(employee, EmployeeResponseDto.class);
    }

    @Transactional
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto employeeRequestDto) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        existingEmployee.setFullName(employeeRequestDto.getFullName());
        existingEmployee.setDepartment(employeeRequestDto.getDepartment());
        existingEmployee.setContactNumber(employeeRequestDto.getContactNumber());
        existingEmployee.setEmail(employeeRequestDto.getEmail());
        existingEmployee.setDateOfJoining(employeeRequestDto.getDateOfJoining());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return modelMapper.map(updatedEmployee, EmployeeResponseDto.class);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        employeeRepository.delete(employee);
    }

    public List<EmployeeResponseDto> searchEmployeesByName(String name) {
        return employeeRepository.findByFullNameContainingIgnoreCase(name).stream()
                .map(emp -> modelMapper.map(emp, EmployeeResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<EmployeeResponseDto> filterEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department).stream()
                .map(emp -> modelMapper.map(emp, EmployeeResponseDto.class))
                .collect(Collectors.toList());
    }
}
