package com.midterm.group4.controller;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.dto.CustomerMapper;
import com.midterm.group4.dto.request.CreateCustomerDTO;
import com.midterm.group4.dto.response.ReadCustomerDTO;
import com.midterm.group4.exception.ObjectNotFoundException;
import com.midterm.group4.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
 class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerMapper customerMapper;

    private UUID customerId;
    private Customer customer;
    private ReadCustomerDTO readCustomerDTO;

    @BeforeEach
    public void setup() {
        customerId = UUID.randomUUID();
        customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhone("1234567890");
        customer.setActive(true);

        readCustomerDTO = new ReadCustomerDTO();
        readCustomerDTO.setCustomerId(customerId);
        readCustomerDTO.setFirstName("John");
        readCustomerDTO.setLastName("Doe");

        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testGetCustomerById() throws Exception {
        when(customerService.findById(customerId)).thenReturn(customer);
        when(customerMapper.toReadDto(customer)).thenReturn(readCustomerDTO);

        mockMvc.perform(get("/api/v1/customer/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
     void testGetCustomerById_NotFound() throws Exception {
        when(customerService.findById(customerId)).thenThrow(new ObjectNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/v1/customer/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
     void testCreateCustomer() throws Exception {
        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO();
        createCustomerDTO.setFirstName("Jane");
        createCustomerDTO.setLastName("Doe");
        createCustomerDTO.setPhone("0987654321");

        when(customerMapper.toEntity(any(CreateCustomerDTO.class))).thenReturn(customer);
        when(customerService.saveCustomer(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toReadDto(any(Customer.class))).thenReturn(readCustomerDTO);

        mockMvc.perform(post("/api/v1/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Jane\", \"lastName\":\"Doe\", \"phone\":\"0987654321\"}"))
                .andExpect(status().isCreated());
    }

    @Test
     void testCustomerActivation() throws Exception {
        when(customerService.findById(customerId)).thenReturn(customer);
        when(customerMapper.toReadDto(any(Customer.class))).thenReturn(readCustomerDTO);

        mockMvc.perform(post("/api/v1/customer/{id}/activate", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
     void testCustomerDeactivation() throws Exception {
        when(customerService.findById(customerId)).thenReturn(customer);
        when(customerMapper.toReadDto(any(Customer.class))).thenReturn(readCustomerDTO);

        mockMvc.perform(post("/api/v1/customer/{id}/deactivate", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
     void testUpdateCustomer() throws Exception {
        CreateCustomerDTO updateCustomerDTO = new CreateCustomerDTO();
        updateCustomerDTO.setFirstName("Updated");
        updateCustomerDTO.setLastName("Name");
        updateCustomerDTO.setPhone("1122334455");

        when(customerService.update(any(UUID.class), any(Customer.class))).thenReturn(customer);
        when(customerMapper.toReadDto(any(Customer.class))).thenReturn(readCustomerDTO);

        mockMvc.perform(put("/api/v1/customer/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Updated\", \"lastName\":\"Name\", \"phone\":\"1122334455\"}"))
                .andExpect(status().isAccepted());
    }
}
