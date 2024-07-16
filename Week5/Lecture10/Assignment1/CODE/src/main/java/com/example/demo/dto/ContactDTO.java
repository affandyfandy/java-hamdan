package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {
    private String id;
    private String name;
    private Date dob;
    private String address;
    private String department;
    private Double salary;
    private String email;
    private String phone;
}
