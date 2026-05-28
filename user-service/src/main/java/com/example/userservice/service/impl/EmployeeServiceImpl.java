package com.example.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.userservice.entity.Employee;
import com.example.userservice.mapper.EmployeeMapper;
import com.example.userservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;


    @Override
    public Employee findByUsername(String username) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, username);
        return employeeMapper.selectOne(wrapper);
    }

    @Override
    public Employee findById(Long id) {
        return employeeMapper.selectById(id);
    }
}