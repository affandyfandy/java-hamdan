package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.example.demo.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
    List<Contact> findByDepartment(String department);
}
