package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/contact")
@AllArgsConstructor
public class ContactController {

    @Autowired
    private final ContactRepository contactRepository;

    @GetMapping
    public ResponseEntity<List<Contact>> listAllContact(){
        List<Contact> listContact= contactRepository.findAll();
        if(listContact.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listContact);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Contact> findContact(@PathVariable("id") String id) {
        Optional<Contact> contactOpt= contactRepository.findById(id);
        if(contactOpt.isPresent()) {
            return ResponseEntity.ok(contactOpt.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Contact> saveContact(@RequestBody Contact contact) {
        Optional<Contact> contactOpt = contactRepository.findById(contact.getId());
        if(contactOpt.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(contactRepository.save(contact));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable(value = "id") String id,
                                                 @RequestBody Contact contactForm) {
        Optional<Contact> contactOpt = contactRepository.findById(id);
        if(contactOpt.isPresent()) {
            Contact contact = contactOpt.get();
            contact.setName(contactForm.getName());
            contact.setAge(contactForm.getAge());

            Contact updatedContact = contactRepository.save(contact);
            return ResponseEntity.ok(updatedContact);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Contact> deleteContact(@PathVariable(value = "id") String id) {
        Optional<Contact> contactOpt = contactRepository.findById(id);
        if(contactOpt.isPresent()) {
            contactRepository.delete(contactOpt.get());
            return ResponseEntity.ok().build();

        }
        return ResponseEntity.notFound().build();
    }
}