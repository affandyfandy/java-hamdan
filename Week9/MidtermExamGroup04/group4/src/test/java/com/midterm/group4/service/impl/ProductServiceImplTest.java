package com.midterm.group4.service.impl;

import com.midterm.group4.data.model.Product;
import com.midterm.group4.data.repository.ProductRepository;
import com.midterm.group4.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

 class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private UUID productId;
    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productId = UUID.randomUUID();

        product = new Product();
        product.setProductId(productId);
        product.setName("Test Product");
        product.setPrice(BigInteger.valueOf(1000));
        product.setQuantity(10);
        product.setActive(true);
        product.setCreatedTime(LocalDateTime.now());
        product.setUpdatedTime(LocalDateTime.now());
    }

    @Test
     void testFindAllSorted() {
        product.setProductId(UUID.randomUUID());
        product.setName("Test Product");

        List<Product> products = List.of(product);
        Page<Product> page = new PageImpl<>(products);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);


        Page<Product> result = productService.findAllSorted(0, 10, "name", "asc");


        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getName());

        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }


    @Test
     void testFindById_Success() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.findById(productId);

        assertNotNull(result);
        assertEquals(product.getProductId(), result.getProductId());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
     void testFindById_NotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> productService.findById(productId));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
     void testSaveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.save(product);

        assertNotNull(result);
        assertEquals(product.getProductId(), result.getProductId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
     void testUpdateProduct_Success() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(BigInteger.valueOf(2000));
        updatedProduct.setQuantity(20);

        Product result = productService.update(productId, updatedProduct);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals(BigInteger.valueOf(2000), result.getPrice());
        assertEquals(20, result.getQuantity());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
     void testUpdateProduct_NotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");

        assertThrows(ObjectNotFoundException.class, () -> productService.update(productId, updatedProduct));
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
     void testUpdateStatus_Success() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.updateStatus(productId, false);

        assertFalse(product.isActive());
        verify(productRepository, times(1)).save(product);
    }

    @Test
     void testFindAllByQuery() {
        Page<Product> page = new PageImpl<>(Collections.singletonList(product));

        when(productRepository.findAllByNameAndStatus(anyString(), anyBoolean(), any(Pageable.class))).thenReturn(page);
        when(productRepository.findAllByName(anyString(), any(Pageable.class))).thenReturn(page);
        when(productRepository.findAllByStatus(anyInt(), any(Pageable.class))).thenReturn(page);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);


        Page<Product> result = productService.findAllByQuery(0, 10, "asc", "name", "Test", "active");
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAllByNameAndStatus(anyString(), anyBoolean(), any(Pageable.class));

        result = productService.findAllByQuery(0, 10, "asc", "name", "Test", null);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAllByName(anyString(), any(Pageable.class));

        result = productService.findAllByQuery(0, 10, "asc", "name", null, "active");
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAllByStatus(anyInt(), any(Pageable.class));

        result = productService.findAllByQuery(0, 10, "asc", "name", null, null);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
     void testSaveAll() {
        List<Product> products = Collections.singletonList(product);
        when(productRepository.saveAll(products)).thenReturn(products);

        productService.saveAll(products);

        verify(productRepository, times(1)).saveAll(products);
    }
}
