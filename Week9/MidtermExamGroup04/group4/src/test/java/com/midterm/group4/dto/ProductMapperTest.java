package com.midterm.group4.dto;

import com.midterm.group4.data.model.Product;
import com.midterm.group4.dto.request.CreateProductDTO;
import com.midterm.group4.dto.response.ReadProductDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

 class ProductMapperTest {

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Test
     void testToEntity() {
        CreateProductDTO dto = new CreateProductDTO("Product A", 10, BigInteger.valueOf(1000));

        Product product = productMapper.toEntity(dto);

        assertEquals("Product A", product.getName());
        assertEquals(10, product.getQuantity());
        assertEquals(BigInteger.valueOf(1000), product.getPrice());
    }

    @Test
     void testToReadProductDto() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setProductId(productId);
        product.setName("Product A");
        product.setActive(true);
        product.setPrice(BigInteger.valueOf(1000));

        ReadProductDTO dto = productMapper.toReadProductDto(product);

        assertEquals(productId, dto.getProductId());
        assertEquals("Product A", dto.getName());
        assertTrue(dto.isActive());
        assertEquals(BigInteger.valueOf(1000), dto.getPrice());
    }

    @Test
     void testToListReadProductDto() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setProductId(productId);
        product.setName("Product A");
        product.setActive(true);
        product.setPrice(BigInteger.valueOf(1000));

        List<ReadProductDTO> dtoList = productMapper.toListReadProductDto(List.of(product));

        assertEquals(1, dtoList.size());
        assertEquals(productId, dtoList.get(0).getProductId());
    }
}
