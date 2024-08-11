package com.midterm.group4.repository;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.data.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhone("1234567890");
        customer.setActive(true);
        customer = customerRepository.save(customer);

        assertNotNull(customer.getCustomerId());
    }

    @Test
    void testFindById_CustomerExists() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhone("1234567890");
        customer.setActive(true);
        customer = customerRepository.save(customer);

        Optional<Customer> foundCustomer = customerRepository.findById(customer.getCustomerId());

        assertTrue(foundCustomer.isPresent());
        assertEquals("John", foundCustomer.get().getFirstName());
    }

    @Test
    void testFindById_CustomerNotFound() {
        Optional<Customer> foundCustomer = customerRepository.findById(UUID.randomUUID());
        assertFalse(foundCustomer.isPresent());
    }

    @Test
    void testFindAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        customer1.setPhone("1234567890");
        customer1.setActive(true);
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setFirstName("Jane");
        customer2.setLastName("Doe");
        customer2.setPhone("0987654321");
        customer2.setActive(true);
        customerRepository.save(customer2);

        List<Customer> customers = customerRepository.findAll();

        assertEquals(2, customers.size());
    }
}
