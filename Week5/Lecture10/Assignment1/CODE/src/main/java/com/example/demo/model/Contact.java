package com.example.demo.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Contact {

    @Id
    private String id;

    @NotNull
    @Size(min = 1, message = "Name should not be null")
    private String name;

    @NotNull
    private Date dob;

    @NotNull
    @Size(min = 1, message = "Address should not be null")
    private String address;

    @NotNull
    @Size(min = 1, message = "Department should not be null")
    private String department;

    @NotNull
    private Double salary;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^\\+62[0-9]{10,}$", message = "Phone number should follow Indonesian format")
    private String phone;

    public Contact() {
    }

    public Contact(String id, String name, Date dob, String address, String department, Double salary, String email, String phone) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.department = department;
        this.salary = salary;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
