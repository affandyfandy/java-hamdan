package com.example.demo.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
