package model;

import java.time.LocalDate;

public class Employee {
    private String id;
    private String name;
    private LocalDate dateOfBirth;
    private String address;
    private String department;

    public Employee(String id, String name, LocalDate dateOfBirth, String address, String department) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDepartment() {
        return department;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}

