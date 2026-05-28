package com.example.fjm0313_takeout_self.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.entity.Employee;

public interface EmployeeService {
    Employee findByUsername(String username);
    Employee findById(Long id);
}