package com.midterm.group4.repository;

import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.data.model.Product;
import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.repository.ProductRepository;
import com.midterm.group4.data.repository.InvoiceRepository;
import com.midterm.group4.data.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderItemRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @BeforeEach
    public void setUp() {
        Product product = new Product();
        product.setName("Product A");
        product.setPrice(BigInteger.valueOf(500));
        product.setQuantity(10);
        product.setActive(true);
        product.setCreatedTime(LocalDateTime.now());
        product.setUpdatedTime(LocalDateTime.now());
        product = productRepository.save(product);

        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setTotalAmount(BigInteger.valueOf(5000));
        invoice.setCreatedTime(LocalDateTime.now());
        invoice.setUpdatedTime(LocalDateTime.now());
        invoice = invoiceRepository.save(invoice);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setInvoice(invoice);
        orderItem.setQuantity(5);
        orderItem.setAmount(BigInteger.valueOf(2500));
        orderItem.setCreatedTime(LocalDateTime.now());
        orderItem.setUpdatedTime(LocalDateTime.now());
        orderItemRepository.save(orderItem);
    }

    @Test
    void testFindSoldProducts() {
        List<String> soldProducts = orderItemRepository.findSoldProducts();
        assertThat(soldProducts).isNotEmpty();
    }

    @Test
    void testFindTopProductsByAmount() {
        List<Object[]> topProducts = orderItemRepository.findTopProductsByAmount();
        assertThat(topProducts).isNotEmpty();
        assertThat(topProducts.get(0)[0]).isEqualTo("Product A");
    }

    @Test
    void testFindTotalQuantityPerProduct() {
        List<Object[]> totalQuantities = orderItemRepository.findTotalQuantityPerProduct();
        assertThat(totalQuantities).isNotEmpty();
        assertThat(totalQuantities.get(0)[0]).isEqualTo("Product A");
        assertThat(((Number) totalQuantities.get(0)[1]).intValue()).isEqualTo(5);
    }
}
