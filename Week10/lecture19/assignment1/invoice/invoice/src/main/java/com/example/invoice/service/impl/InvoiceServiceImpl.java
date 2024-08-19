package com.example.invoice.service.impl;

import com.example.invoice.dto.InvoiceDTO;
import com.example.invoice.dto.ProductDTO;
import com.example.invoice.model.Invoice;
import com.example.invoice.model.Product;
import com.example.invoice.repository.InvoiceRepository;
import com.example.invoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO getInvoiceById(Long id) {
        return invoiceRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        Invoice invoice = convertToEntity(invoiceDTO);
        invoice = invoiceRepository.save(invoice);
        return convertToDTO(invoice);
    }

    private InvoiceDTO convertToDTO(Invoice invoice) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(invoice.getId());
        invoiceDTO.setProducts(invoice.getProducts().stream().map(this::convertProductToDTO).collect(Collectors.toList()));
        return invoiceDTO;
    }

    private Invoice convertToEntity(InvoiceDTO invoiceDTO) {
        Invoice invoice = new Invoice();
        invoice.setProducts(invoiceDTO.getProducts().stream().map(this::convertProductToEntity).collect(Collectors.toList()));
        return invoice;
    }

    private ProductDTO convertProductToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        return productDTO;
    }

    private Product convertProductToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        return product;
    }
}
