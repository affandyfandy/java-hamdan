package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, String> {
    List<Contact> findByDepartment(String department);
}
