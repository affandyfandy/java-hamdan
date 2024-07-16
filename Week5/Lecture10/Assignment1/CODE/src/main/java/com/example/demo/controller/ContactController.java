package com.example.demo.controller;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import com.example.demo.dto.ContactDTO;
import com.example.demo.mapper.ContactMapper;
import com.example.demo.model.Contact;
import com.example.demo.repository.ContactRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/contacts")
@Validated
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@Valid @RequestBody ContactDTO contactDTO) {
        Contact contact = ContactMapper.INSTANCE.contactDTOToContact(contactDTO);
        Contact savedContact = contactRepository.save(contact);
        return ResponseEntity.ok(ContactMapper.INSTANCE.contactToContactDTO(savedContact));
    }

    @GetMapping
    public List<ContactDTO> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        System.out.println("Fetched contacts: " + contacts);
        List<ContactDTO> contactDTOs = contacts.stream()
                .map(ContactMapper.INSTANCE::contactToContactDTO)
                .collect(Collectors.toList());
        System.out.println("Mapped ContactDTOs: " + contactDTOs);
        return contactDTOs;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable String id) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isPresent()) {
            return ResponseEntity.ok(ContactMapper.INSTANCE.contactToContactDTO(contact.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(@PathVariable String id, @Valid @RequestBody ContactDTO contactDTO) {
        if (!contactRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Contact contact = ContactMapper.INSTANCE.contactDTOToContact(contactDTO);
        contact.setId(id);
        Contact updatedContact = contactRepository.save(contact);
        return ResponseEntity.ok(ContactMapper.INSTANCE.contactToContactDTO(updatedContact));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) {
        if (!contactRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        contactRepository.deleteById(id);
        return ResponseEntity.noContent().build();
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
                    continue;
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
                contact.setSalary(row.getCell(5).getNumericCellValue());
                contact.setEmail(row.getCell(6).getStringCellValue());
                contact.setPhone(row.getCell(7).getStringCellValue());

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
