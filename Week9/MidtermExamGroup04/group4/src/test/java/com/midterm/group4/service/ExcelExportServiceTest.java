package com.midterm.group4.service;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.data.model.Product;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ExcelExportServiceTest {

    private ExcelExportService excelExportService;

    @BeforeEach
    public void setUp() {
        excelExportService = new ExcelExportService();
    }

    @Test
    void testExportInvoice() throws Exception {
        List<Invoice> invoices = createDummyInvoices();

        ByteArrayOutputStream outputStream = excelExportService.exportInvoice(invoices);

        assertNotNull(outputStream);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(outputStream.toByteArray()))) {
            assertEquals(1, workbook.getNumberOfSheets());
            assertEquals("Invoices", workbook.getSheetAt(0).getSheetName());

            int expectedRowCount = invoices.size();
            assertEquals(expectedRowCount, workbook.getSheetAt(0).getLastRowNum());
        }
    }

    @Test
    void testExportInvoicesToExcel() throws IOException {
        List<Invoice> invoices = createDummyInvoices();
        ByteArrayInputStream inputStream = excelExportService.exportInvoicesToExcel(invoices);

        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        int numberOfRows = sheet.getPhysicalNumberOfRows();

        assertEquals(4, numberOfRows);
    }

    private List<Invoice> createDummyInvoices() {
        Customer customer = new Customer();
        customer.setCustomerId(UUID.randomUUID());
        customer.setFirstName("John");
        customer.setLastName("Doe");

        Product product1 = new Product();
        product1.setProductId(UUID.randomUUID());
        product1.setName("Product 1");
        product1.setPrice(BigInteger.valueOf(100));

        Product product2 = new Product();
        product2.setProductId(UUID.randomUUID());
        product2.setName("Product 2");
        product2.setPrice(BigInteger.valueOf(200));

        Product product3 = new Product();
        product3.setProductId(UUID.randomUUID());
        product3.setName("Product 3");
        product3.setPrice(BigInteger.valueOf(150));

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setProduct(product1);
        orderItem1.setQuantity(2);
        orderItem1.setAmount(BigInteger.valueOf(200));

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setProduct(product2);
        orderItem2.setQuantity(3);
        orderItem2.setAmount(BigInteger.valueOf(600));

        OrderItem orderItem3 = new OrderItem();
        orderItem3.setProduct(product3);
        orderItem3.setQuantity(1);
        orderItem3.setAmount(BigInteger.valueOf(150));

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());
        invoice.setCustomer(customer);
        invoice.setTotalAmount(BigInteger.valueOf(950));
        invoice.setListOrderItem(List.of(orderItem1, orderItem2, orderItem3));

        return new ArrayList<>(List.of(invoice));
    }

}
