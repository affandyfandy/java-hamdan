package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("source", "fpt-software");
        return ResponseEntity.ok().headers(headers).body(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("source", "fpt-software");
        return ResponseEntity.ok().headers(headers).body(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("source", "fpt-software");
        return ResponseEntity.ok().headers(headers).body(employeeService.saveEmployee(employee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("source", "fpt-software");
        employee.setId(id);
        return ResponseEntity.ok().headers(headers).body(employeeService.updateEmployee(employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("source", "fpt-software");
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().headers(headers).build();
    }
}
