package com.midterm.group4.dto;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.dto.request.CreateInvoiceDTO;
import com.midterm.group4.dto.response.ReadInvoiceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@SpringJUnitConfig
@ComponentScan(basePackages = "com.midterm.group4")
 class InvoiceMapperTest {

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @BeforeEach
    public void setup() {
    }

    @Test
     void testToReadInvoiceDto() {
        UUID invoiceId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setTotalAmount(BigInteger.valueOf(1000));
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setCustomer(customer);

        ReadInvoiceDTO dto = invoiceMapper.toReadInvoiceDto(invoice);

        assertEquals(invoiceId, dto.getInvoiceId());
        assertEquals(BigInteger.valueOf(1000), dto.getTotalAmount());
        assertEquals(LocalDate.now(), dto.getInvoiceDate());
        assertEquals(customerId, dto.getCustomer().getCustomerId());
    }

    @Test
     void testToListReadInvoiceDto() {
        UUID invoiceId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setTotalAmount(BigInteger.valueOf(1000));
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setCustomer(customer);

        List<ReadInvoiceDTO> dtoList = invoiceMapper.toListReadInvoiceDto(List.of(invoice));

        assertEquals(1, dtoList.size());
        assertEquals(invoiceId, dtoList.get(0).getInvoiceId());
        assertEquals(customerId, dtoList.get(0).getCustomer().getCustomerId());
    }

    @Test
     void testToEntity() {
        UUID customerId = UUID.randomUUID();
        CreateInvoiceDTO dto = new CreateInvoiceDTO(customerId, null);

        Invoice invoice = invoiceMapper.toEntity(dto);

        assertEquals(customerId, invoice.getCustomer().getCustomerId());
    }
}
