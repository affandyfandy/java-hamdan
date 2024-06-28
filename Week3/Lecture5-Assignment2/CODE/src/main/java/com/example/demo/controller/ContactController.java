package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
            contact.setDob(contactForm.getDob());
            contact.setAddress(contactForm.getAddress());
            contact.setDepartment(contactForm.getDepartment());

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

    
    @PostMapping("/import")
    public ResponseEntity<String> importContactsFromFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }
                Contact contact = new Contact();
                contact.setId(row.getCell(0).getStringCellValue());
                contact.setName(row.getCell(1).getStringCellValue());
                
                Cell dateCell = row.getCell(2);
                if (dateCell != null) {
                    Date dob = null;
                    if (dateCell.getCellType() == CellType.STRING) {
                        dob = dateFormat.parse(dateCell.getStringCellValue());
                    } else if (dateCell.getCellType() == CellType.NUMERIC) {
                        dob = dateCell.getDateCellValue();
                    }
                    contact.setDob(dob);
                }

                contact.setAddress(row.getCell(3).getStringCellValue());
                contact.setDepartment(row.getCell(4).getStringCellValue());
                
                contactRepository.save(contact);
            }
            return ResponseEntity.status(HttpStatus.OK).body("Contacts imported successfully");

        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Date format error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }

    @GetMapping("/by-department")
    public ResponseEntity<List<Contact>> getContactsByDepartment(@RequestParam("department") String department) {
        List<Contact> contacts = contactRepository.findByDepartment(department);
        if (contacts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contacts);
    }
}