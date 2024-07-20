package lecture7.assignment1.model;

import lecture7.assignment1.controller.EmployeeWork;

public class Employee {
    private int id;
    private String name;
    private int age;
    private EmployeeWork employeeWork;

    // Constructor
    public Employee(int id, String name, int age, EmployeeWork employeeWork) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.employeeWork = employeeWork;
    }

    public void working() {
        System.out.println("My ID is: " + id);
        System.out.println("My Name is: " + name);
        System.out.println("My Age is: " + age);
        employeeWork.work();
    }
}