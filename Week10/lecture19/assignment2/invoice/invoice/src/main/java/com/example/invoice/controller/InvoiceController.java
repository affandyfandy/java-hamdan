package com.example.invoice.controller;

import com.example.invoice.dto.InvoiceDTO;
import com.example.invoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public InvoiceDTO getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id);
    }

    @PostMapping
    public InvoiceDTO createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.createInvoice(invoiceDTO);
    }
}
