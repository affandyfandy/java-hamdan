package com.example.lecture8_assignment2.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.lecture8_assignment2.model.Employee;

public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setName(rs.getString("name"));
        employee.setEmail(rs.getString("email"));
        employee.setDepartment(rs.getString("department"));
        employee.setSalary(rs.getDouble("salary"));
        return employee;
    }
}
