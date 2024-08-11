package com.midterm.group4.repository;

import com.midterm.group4.data.model.Product;
import com.midterm.group4.data.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setName("Product A");
        product.setPrice(BigInteger.valueOf(1000));
        product.setQuantity(100);
        product.setActive(true);
        product = productRepository.save(product);

        assertNotNull(product.getProductId());
    }

    @Test
    void testFindAllByName() {
        Product product = new Product();
        product.setName("Product A");
        product.setPrice(BigInteger.valueOf(1000));
        product.setQuantity(100);
        product.setActive(true);
        productRepository.save(product);

        Page<Product> products = productRepository.findAllByName("Product A", PageRequest.of(0, 10));

        assertEquals(1, products.getTotalElements());
        assertEquals("Product A", products.getContent().get(0).getName());
    }

    @Test
    void testFindAllByStatus() {
        Product product = new Product();
        product.setName("Product A");
        product.setPrice(BigInteger.valueOf(1000));
        product.setQuantity(100);
        product.setActive(true);
        productRepository.save(product);

        Page<Product> products = productRepository.findAllByStatus(1, PageRequest.of(0, 10));

        assertEquals(1, products.getTotalElements());
        assertTrue(products.getContent().get(0).isActive());
    }

    @Test
    void testFindAllByNameAndStatus() {
        Product product = new Product();
        product.setName("Product A");
        product.setPrice(BigInteger.valueOf(1000));
        product.setQuantity(100);
        product.setActive(true);
        productRepository.save(product);

        Page<Product> products = productRepository.findAllByNameAndStatus("Product A", true, PageRequest.of(0, 10));

        assertEquals(1, products.getTotalElements());
        assertEquals("Product A", products.getContent().get(0).getName());
        assertTrue(products.getContent().get(0).isActive());
    }
}
