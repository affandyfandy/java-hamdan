package lecture16.invoice.dto;

import java.util.List;

import lombok.Data;

@Data
public class InvoiceDTO {
    private Long id;
    private List<ProductDTO> products;
}
