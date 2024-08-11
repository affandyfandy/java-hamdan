package com.midterm.group4.utils;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DocumentUtilsTest {

    private DocumentUtils documentUtils;
    private SpringTemplateEngine templateEngine;

    @BeforeEach
    void setUp() {
        templateEngine = Mockito.mock(SpringTemplateEngine.class);
        documentUtils = new DocumentUtils(templateEngine);
    }

    @Test
    void testGenerateByteInvoice() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());
        invoice.setTotalAmount(BigInteger.valueOf(1000));
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setCreatedTime(LocalDateTime.now());
        invoice.setUpdatedTime(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItems.add(orderItem);
        invoice.setListOrderItem(orderItems);

        when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("<html><body>Test Invoice</body></html>");

        byte[] pdfBytes = documentUtils.generateByteInvoice(invoice);

        assertNotNull(pdfBytes);
        assert(pdfBytes.length > 0);
    }
}
