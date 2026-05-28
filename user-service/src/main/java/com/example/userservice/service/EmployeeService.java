package com.example.userservice.service;

import com.example.userservice.entity.Employee;

public interface EmployeeService {
    Employee findByUsername(String username);
    Employee findById(Long id);
}