package com.midterm.group4.repository;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.repository.CustomerRepository;
import com.midterm.group4.data.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSaveInvoice() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhone("1234567890");
        customer.setActive(true);
        customer = customerRepository.save(customer);

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setTotalAmount(BigInteger.valueOf(1000));
        invoice.setInvoiceDate(LocalDate.now());
        invoice = invoiceRepository.save(invoice);

        assertNotNull(invoice.getInvoiceId());
        assertEquals(customer.getCustomerId(), invoice.getCustomer().getCustomerId());
    }

    @Test
    void testFindAllByCustomerId() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhone("1234567890");
        customer.setActive(true);
        customer = customerRepository.save(customer);

        Invoice invoice1 = new Invoice();
        invoice1.setCustomer(customer);
        invoice1.setTotalAmount(BigInteger.valueOf(1000));
        invoice1.setInvoiceDate(LocalDate.now());
        invoiceRepository.save(invoice1);

        Invoice invoice2 = new Invoice();
        invoice2.setCustomer(customer);
        invoice2.setTotalAmount(BigInteger.valueOf(2000));
        invoice2.setInvoiceDate(LocalDate.now());
        invoiceRepository.save(invoice2);

        List<Invoice> invoices = invoiceRepository.findByCustomer(customer.getCustomerId());

        assertEquals(2, invoices.size());
    }

    @Test
    void testFindByMonthAndYear() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhone("1234567890");
        customer.setActive(true);
        customer = customerRepository.save(customer);

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setTotalAmount(BigInteger.valueOf(1000));
        invoice.setInvoiceDate(LocalDate.now());
        invoiceRepository.save(invoice);

        List<Invoice> invoices = invoiceRepository.findByMonthAndYear(
                LocalDate.now().getMonthValue(),
                LocalDate.now().getYear()
        );

        assertEquals(1, invoices.size());
    }

    @Test
    void testFindTotalAmountByDate() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhone("1234567890");
        customer.setActive(true);
        customer = customerRepository.save(customer);

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setTotalAmount(BigInteger.valueOf(1000));
        invoice.setInvoiceDate(LocalDate.now());
        invoiceRepository.save(invoice);

        BigInteger totalAmount = invoiceRepository.findTotalAmountByDate(LocalDate.now());

        assertEquals(BigInteger.valueOf(1000), totalAmount);
    }
}
