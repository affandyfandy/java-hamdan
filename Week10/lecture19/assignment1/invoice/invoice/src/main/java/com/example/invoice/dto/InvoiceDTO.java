package com.example.invoice.dto;

import lombok.Data;
import java.util.List;

@Data
public class InvoiceDTO {
    private Long id;
    private List<ProductDTO> products;
}
