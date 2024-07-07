package com.example.lecture8_assignment3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lecture8_assignment3.model.Employee;
import com.example.lecture8_assignment3.repository.EmployeeRepository2;

@Service
public class EmployeeService2 {

    @Autowired
    private EmployeeRepository2 employeeRepository2;

    public List<Employee> getAllEmployees() {
        return employeeRepository2.findAll();
    }

    public Employee getEmployeeById(int id) {
        return employeeRepository2.findById(id);
    }

    @Transactional("transactionManager2")
    public void saveEmployee(Employee employee) {
        employeeRepository2.save(employee);
    }

    @Transactional("transactionManager2")
    public void updateEmployee(Employee employee) {
        employeeRepository2.update(employee);
    }

    @Transactional("transactionManager2")
    public void deleteEmployee(int id) {
        employeeRepository2.deleteById(id);
    }
}
