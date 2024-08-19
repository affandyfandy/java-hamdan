package com.example.invoice.service;

import com.example.invoice.dto.InvoiceDTO;

import java.util.List;

public interface InvoiceService {
    List<InvoiceDTO> getAllInvoices();
    InvoiceDTO getInvoiceById(Long id);
    InvoiceDTO createInvoice(InvoiceDTO invoiceDTO);
}
