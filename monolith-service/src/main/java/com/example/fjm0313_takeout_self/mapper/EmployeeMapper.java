package com.example.fjm0313_takeout_self.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.fjm0313_takeout_self.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
