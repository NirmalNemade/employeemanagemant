package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    List<Employee> findByFullNameContainingIgnoreCase(String fullName);
    
    List<Employee> findByDepartment(String department);
}