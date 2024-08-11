package com.midterm.group4.dto;

import com.midterm.group4.data.model.Customer;
import com.midterm.group4.dto.request.CreateCustomerDTO;
import com.midterm.group4.dto.response.ReadCustomerDTO;
import com.midterm.group4.dto.response.ReadCustomerOrderDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerMapperTest {

    private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    void testToReadDto() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhone("123456789");

        ReadCustomerDTO dto = customerMapper.toReadDto(customer);

        assertEquals(customerId, dto.getCustomerId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("123456789", dto.getPhone());
    }

    @Test
    void testToEntity() {
        CreateCustomerDTO dto = new CreateCustomerDTO("John", "Doe", "123456789");

        Customer customer = customerMapper.toEntity(dto);

        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("123456789", customer.getPhone());
    }

    @Test
    void testToListReadDto() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setPhone("123456789");

        List<ReadCustomerDTO> dtoList = customerMapper.toListReadDto(List.of(customer));

        assertEquals(1, dtoList.size());
        assertEquals(customerId, dtoList.get(0).getCustomerId());
    }

    @Test
    void testToReadCustomerOrderDto() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setFirstName("John");
        customer.setLastName("Doe");

        ReadCustomerOrderDTO dto = customerMapper.toReadCustomerOrderDto(customer);

        assertEquals(customerId, dto.getCustomerId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
    }
}
