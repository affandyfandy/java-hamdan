package com.midterm.group4.service.impl;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.data.repository.CustomerRepository;
import com.midterm.group4.exception.InvalidInputException;
import com.midterm.group4.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

 class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private UUID customerId;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customerId = UUID.randomUUID();

        customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhone("+1234567890");
        customer.setActive(true);
        customer.setCreatedTime(LocalDateTime.now());
        customer.setUpdatedTime(LocalDateTime.now());
    }

    @Test
     void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> page = new PageImpl<>(Collections.singletonList(customer));
        when(customerRepository.findAll(pageable)).thenReturn(page);

        Page<Customer> result = customerService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(customerRepository, times(1)).findAll(pageable);
    }

    @Test
     void testFindById_Success() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Customer result = customerService.findById(customerId);

        assertNotNull(result);
        assertEquals(customer.getCustomerId(), result.getCustomerId());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
     void testFindById_NotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> customerService.findById(customerId));
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
     void testSaveCustomer_Success() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.saveCustomer(customer);

        assertNotNull(result);
        assertEquals(customer.getCustomerId(), result.getCustomerId());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
     void testSaveCustomer_InvalidPhone() {
        customer.setPhone("invalid_phone");

        assertThrows(InvalidInputException.class, () -> customerService.saveCustomer(customer));
        verify(customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
     void testUpdateStatus_Success() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        customerService.updateStatus(customerId, false);

        assertFalse(customer.isActive());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
     void testUpdateCustomer_Success() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Customer updatedCustomer = new Customer();
        updatedCustomer.setFirstName("Jane");
        updatedCustomer.setLastName("Doe");
        updatedCustomer.setPhone("+0987654321");

        Customer result = customerService.update(customerId, updatedCustomer);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("+0987654321", result.getPhone());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
     void testUpdateCustomer_NotFound() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Customer updatedCustomer = new Customer();
        updatedCustomer.setFirstName("Jane");
        updatedCustomer.setLastName("Doe");

        assertThrows(ObjectNotFoundException.class, () -> customerService.update(customerId, updatedCustomer));
        verify(customerRepository, times(0)).save(any(Customer.class));
    }
}
