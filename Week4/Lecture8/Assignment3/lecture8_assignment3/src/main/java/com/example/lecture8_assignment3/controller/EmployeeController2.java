package com.example.lecture8_assignment3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lecture8_assignment3.model.Employee;
import com.example.lecture8_assignment3.service.EmployeeService2;

@RestController
@RequestMapping("/v1/employees2")
public class EmployeeController2 {

    @Autowired
    private EmployeeService2 employeeService2;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService2.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return employeeService2.getEmployeeById(id);
    }

    @PostMapping
    public void createEmployee(@RequestBody Employee employee) {
        employeeService2.saveEmployee(employee);
    }

    @PutMapping("/{id}")
    public void updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        employee.setId(id);
        employeeService2.updateEmployee(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable int id) {
        employeeService2.deleteEmployee(id);
    }
}
