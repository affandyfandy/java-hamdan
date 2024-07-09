package com.example.lecture8_assignment2.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.lecture8_assignment2.mapper.EmployeeRowMapper;
import com.example.lecture8_assignment2.model.Employee;

@Repository
public class EmployeeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Employee> findAll() {
        String sql = "SELECT * FROM Employee";
        return jdbcTemplate.query(sql, new EmployeeRowMapper());
    }

    public Employee findById(int id) {
        String sql = "SELECT * FROM Employee WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new EmployeeRowMapper());
    }

    public int save(Employee employee) {
        String sql = "INSERT INTO Employee (name, email, department, salary) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, employee.getName(), employee.getEmail(), employee.getDepartment(), employee.getSalary());
    }

    public int update(Employee employee) {
        String sql = "UPDATE Employee SET name = ?, email = ?, department = ?, salary = ? WHERE id = ?";
        return jdbcTemplate.update(sql, employee.getName(), employee.getEmail(), employee.getDepartment(), employee.getSalary(), employee.getId());
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM Employee WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
