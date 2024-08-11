package com.midterm.group4.service.impl;

import com.midterm.group4.data.model.*;
import com.midterm.group4.data.repository.*;
import com.midterm.group4.exception.ObjectNotFoundException;
import com.midterm.group4.service.ProductService;
import com.midterm.group4.utils.DocumentUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InvoiceServiceImplTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private DocumentUtils documentUtils;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private void invokeUpdate(UUID invoiceId, Invoice invoice, OrderItem orderItem) {
        invoiceService.update(invoiceId, invoice, List.of(orderItem));
    }

    @Test
    void testFindById_Success() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        Invoice foundInvoice = invoiceService.findById(invoiceId);

        assertNotNull(foundInvoice);
        assertEquals(invoiceId, foundInvoice.getInvoiceId());
    }

    @Test
    void testFindById_NotFound() {
        UUID invoiceId = UUID.randomUUID();

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> invoiceService.findById(invoiceId));
    }

    @Test
    void testSave_Success() {
        OrderItem orderItem = new OrderItem();
        orderItem.setAmount(BigInteger.valueOf(1000));
        Invoice invoice = new Invoice();
        invoice.setListOrderItem(List.of(orderItem));

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        Invoice savedInvoice = invoiceService.save(invoice);

        assertNotNull(savedInvoice);
        assertEquals(BigInteger.valueOf(1000), savedInvoice.getTotalAmount());
    }

    @Test
    void testSave_EmptyOrderItems() {
        Invoice invoice = new Invoice();
        invoice.setListOrderItem(new ArrayList<>());

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        Invoice savedInvoice = invoiceService.save(invoice);

        assertNotNull(savedInvoice);
        assertEquals(BigInteger.ZERO, savedInvoice.getTotalAmount());
    }

    @Test
    void testUpdate_Success() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setListOrderItem(new ArrayList<>());

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        when(productService.findById(any(UUID.class))).thenReturn(new Product());

        Invoice updatedInvoice = invoiceService.update(invoiceId, invoice, new ArrayList<>());

        assertNotNull(updatedInvoice);
    }

    @Test
    void testUpdate_ProductNotFound() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setListOrderItem(new ArrayList<>());

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(new Product());

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(productService.findById(any(UUID.class))).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> invokeUpdate(invoiceId, invoice, orderItem));
    }

    @Test
     void testUpdate_ProductNotActive() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setListOrderItem(new ArrayList<>());

        Product product = new Product();
        product.setActive(false);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(productService.findById(any(UUID.class))).thenReturn(product);

        assertThrows(IllegalArgumentException.class, () -> invokeUpdate(invoiceId, invoice, orderItem));
    }


    @Test
     void testUpdate_InsufficientQuantity() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setListOrderItem(new ArrayList<>());

        Product product = new Product();
        product.setQuantity(1);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(productService.findById(any(UUID.class))).thenReturn(product);

        assertThrows(IllegalArgumentException.class, () -> invokeUpdate(invoiceId, invoice, orderItem));
    }

    @Test
     void testFindAllSorted_Success() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "invoiceDate"));
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());
        invoice.setListOrderItem(new ArrayList<>());

        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice));

        when(invoiceRepository.findAll(pageable)).thenReturn(invoicePage);

        Page<Invoice> result = invoiceService.findAllSorted(0, 10, "invoiceDate", "ASC");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(invoiceRepository, times(1)).findAll(pageable);
    }


    @Test
     void testFindAllByDate_Success() {
        LocalDate date = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "invoiceDate"));
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());
        invoice.setListOrderItem(new ArrayList<>());

        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice));

        when(invoiceRepository.findByInvoiceDate(date, pageable)).thenReturn(invoicePage);

        Page<Invoice> result = invoiceService.findAllByDate(0, 10, date, "ASC");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(invoiceRepository, times(1)).findByInvoiceDate(date, pageable);
    }


    @Test
     void testFindAllByMonth_Success() {
        int month = 8;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "invoiceDate"));
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());
        invoice.setListOrderItem(new ArrayList<>());

        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice));

        when(invoiceRepository.findByMonth(month, pageable)).thenReturn(invoicePage);

        Page<Invoice> result = invoiceService.findAllByMonth(0, 10, month, "ASC");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(invoiceRepository, times(1)).findByMonth(month, pageable);
    }


    @Test
     void testCreateInvoice_Success() {
        UUID productId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(BigInteger.valueOf(500));
        product.setQuantity(10);
        product.setActive(true);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        Invoice invoice = new Invoice();
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setActive(true);
        invoice.setCustomer(customer);

        when(productService.findById(productId)).thenReturn(product);
        when(orderItemRepository.saveAll(anyList())).thenReturn(List.of(orderItem));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Invoice savedInvoice = invoiceService.createInvoice(invoice, List.of(orderItem));


        assertNotNull(savedInvoice);
        assertEquals(BigInteger.valueOf(1000), savedInvoice.getTotalAmount());
        verify(productRepository, times(1)).save(product);
        verify(invoiceRepository, times(1)).save(invoice);
        verify(orderItemRepository, times(1)).saveAll(anyList());
    }


    @Test
     void testGenerateToPdf_Success() throws IOException {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);

        byte[] pdfBytes = new byte[]{1, 2, 3};

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(documentUtils.generateByteInvoice(invoice)).thenReturn(pdfBytes);

        byte[] generatedPdf = invoiceService.generateToPdf(invoiceId);

        assertNotNull(generatedPdf);
        assertArrayEquals(pdfBytes, generatedPdf);
    }

    @Test
     void testGetInvoicesByFilter_Success() {
        UUID customerId = UUID.randomUUID();
        int month = 8;
        int year = 2024;
        Invoice invoice = new Invoice();
        invoice.setCustomer(new Customer());
        invoice.getCustomer().setCustomerId(customerId);

        when(invoiceRepository.findByCustomerAndMonthAndYear(customerId, month, year))
                .thenReturn(List.of(invoice));

        List<Invoice> result = invoiceService.getInvoicesByFilter(customerId, month, year);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
     void testGetTotalAmountPerDay_Success() {
        LocalDate date = LocalDate.now();
        BigInteger totalAmount = BigInteger.valueOf(5000);

        when(invoiceRepository.findTotalAmountByDate(date)).thenReturn(totalAmount);

        BigInteger result = invoiceService.getTotalAmountPerDay(date);

        assertNotNull(result);
        assertEquals(totalAmount, result);
    }

    @Test
     void testGetTotalAmountPerMonth_Success() {
        int month = 8;
        int year = 2024;
        BigInteger totalAmount = BigInteger.valueOf(15000);

        when(invoiceRepository.findTotalAmountByMonth(month, year)).thenReturn(totalAmount);

        BigInteger result = invoiceService.getTotalAmountPerMonth(month, year);

        assertNotNull(result);
        assertEquals(totalAmount, result);
    }

    @Test
     void testGetTotalAmountPerYear_Success() {
        int year = 2024;
        BigInteger totalAmount = BigInteger.valueOf(100000);

        when(invoiceRepository.findTotalAmountByYear(year)).thenReturn(totalAmount);

        BigInteger result = invoiceService.getTotalAmountPerYear(year);

        assertNotNull(result);
        assertEquals(totalAmount, result);
    }

    @Test
     void testGetTop3ProductsByAmount_Success() {
        Object[] productData = new Object[]{"Product A", BigInteger.valueOf(5000)};
        List<Object[]> mockData = new ArrayList<>();
        mockData.add(productData);

        when(orderItemRepository.findTopProductsByAmount()).thenReturn(mockData);

        List<Map<String, Object>> result = invoiceService.getTop3ProductsByAmount();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product A", result.get(0).get("name"));
        assertEquals(BigInteger.valueOf(5000), result.get(0).get("totalAmount"));
    }

    @Test
     void testGetSoldProducts_Success() {
        when(orderItemRepository.findSoldProducts()).thenReturn(Arrays.asList("Product A", "Product B"));

        List<String> result = invoiceService.getSoldProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product A", result.get(0));
        assertEquals("Product B", result.get(1));
    }

    @Test
     void testGetTotalQuantityPerProduct_Success() {
        when(orderItemRepository.findTotalQuantityPerProduct())
                .thenReturn(List.of(new Object[]{"Product A", 10L}, new Object[]{"Product B", 5L}));

        Map<String, Long> result = invoiceService.getTotalQuantityPerProduct();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(10L, result.get("Product A"));
        assertEquals(5L, result.get("Product B"));
    }

    @Test
     void testGetTotalAmountPerProduct_Success() {
        when(orderItemRepository.findTotalAmountPerProduct())
                .thenReturn(List.of(new Object[]{"Product A", BigInteger.valueOf(10000)},
                        new Object[]{"Product B", BigInteger.valueOf(5000)}));

        Map<String, BigInteger> result = invoiceService.getTotalAmountPerProduct();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(BigInteger.valueOf(10000), result.get("Product A"));
        assertEquals(BigInteger.valueOf(5000), result.get("Product B"));
    }

    @Test
    void testUpdate_InvoiceNotFound() {
        UUID invoiceId = UUID.randomUUID();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> invokeUpdate(invoiceId));
    }

    private void invokeUpdate(UUID invoiceId) {
        invoiceService.update(invoiceId, new Invoice(), new ArrayList<>());
    }

    @Test
     void testFindAllFiltered_ByCustomerIdAndMonth_Success() {
        UUID customerId = UUID.randomUUID();
        String month = "08";
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "invoiceDate"));
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());
        invoice.setListOrderItem(new ArrayList<>());

        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice));

        when(invoiceRepository.findAllByCustomerIdAndMonth(customerId, month, pageable)).thenReturn(invoicePage);

        Page<Invoice> result = invoiceService.findAllFiltered(0, 10, "invoiceDate", "ASC", customerId, null, month);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(invoiceRepository, times(1)).findAllByCustomerIdAndMonth(customerId, month, pageable);
    }


    @Test
     void testCreateInvoice_CustomerNotFound() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(BigInteger.valueOf(500));
        product.setQuantity(10);
        product.setActive(true);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        Invoice invoice = new Invoice();
        invoice.setCustomer(new Customer());
        invoice.getCustomer().setCustomerId(customerId);

        when(productService.findById(productId)).thenReturn(product);
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> invokeCreateInvoice(invoice, orderItem));
    }

    private void invokeCreateInvoice(Invoice invoice, OrderItem orderItem) {
        invoiceService.createInvoice(invoice, List.of(orderItem));
    }

    @Test
     void testCreateInvoice_ProductInactive() {
        UUID productId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(BigInteger.valueOf(500));
        product.setQuantity(10);
        product.setActive(false);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        Invoice invoice = new Invoice();
        invoice.setCustomer(new Customer());
        invoice.getCustomer().setCustomerId(customerId);

        when(productService.findById(productId)).thenReturn(product);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(invoice.getCustomer()));

        assertThrows(IllegalArgumentException.class, () -> invokeCreateInvoice(invoice, orderItem));
    }

    @Test
     void testCreateInvoice_InsufficientProductQuantity() {
        UUID productId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(BigInteger.valueOf(500));
        product.setQuantity(1);
        product.setActive(true);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        Invoice invoice = new Invoice();
        invoice.setCustomer(new Customer());
        invoice.getCustomer().setCustomerId(customerId);

        when(productService.findById(productId)).thenReturn(product);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(invoice.getCustomer()));

        assertThrows(IllegalArgumentException.class, () -> invokeCreateInvoice(invoice, orderItem));
    }

    @Test
     void testGetTotalAmountPerProduct_NoProductsSold() {
        when(orderItemRepository.findTotalAmountPerProduct()).thenReturn(new ArrayList<>());

        Map<String, BigInteger> result = invoiceService.getTotalAmountPerProduct();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
     void testGenerateToPdf_InvoiceNotFound() {
        UUID invoiceId = UUID.randomUUID();

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> invoiceService.generateToPdf(invoiceId));
    }

    @Test
     void testFindAll_NoSortOrder() {
        Pageable pageable = PageRequest.of(0, 10);
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());

        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice));

        when(invoiceRepository.findAll(pageable)).thenReturn(invoicePage);

        Page<Invoice> result = invoiceService.findAll(0, 10, null);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(invoiceRepository, times(1)).findAll(pageable);
    }

    @Test
     void testFindAllByCustomerName_Success() {
        String customerName = "John Doe";
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "invoiceDate"));
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());

        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice));

        when(invoiceRepository.findAllByCustomerName(customerName, pageable)).thenReturn(invoicePage);

        Page<Invoice> result = invoiceService.findAllByCustomerName(0, 10, "invoiceDate", "ASC", customerName);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(invoiceRepository, times(1)).findAllByCustomerName(customerName, pageable);
    }

    @Test
     void testFindAllFiltered_ByCustomerIdAndInvoiceDate_Success() {
        UUID customerId = UUID.randomUUID();
        String invoiceDate = "2024-08-12";
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "invoiceDate"));
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());

        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice));

        when(invoiceRepository.findAllByCustomerIdAndInvoiceDate(customerId, invoiceDate, pageable)).thenReturn(invoicePage);

        Page<Invoice> result = invoiceService.findAllFiltered(0, 10, "invoiceDate", "ASC", customerId, invoiceDate, null);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(invoiceRepository, times(1)).findAllByCustomerIdAndInvoiceDate(customerId, invoiceDate, pageable);
    }

    @Test
     void testGenerateToPdf_IOException() throws IOException {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(documentUtils.generateByteInvoice(invoice)).thenThrow(new IOException("Failed to generate PDF"));

        assertThrows(IOException.class, () -> invoiceService.generateToPdf(invoiceId));
    }

    @Test
     void testGetInvoicesByFilter_ByCustomerIdAndYear_Success() {
        UUID customerId = UUID.randomUUID();
        int year = 2024;
        Invoice invoice = new Invoice();
        invoice.setCustomer(new Customer());
        invoice.getCustomer().setCustomerId(customerId);

        when(invoiceRepository.findByCustomerAndYear(customerId, year))
                .thenReturn(List.of(invoice));

        List<Invoice> result = invoiceService.getInvoicesByFilter(customerId, null, year);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
     void testGetTotalAmountPerProduct_MultipleSales_Success() {
        when(orderItemRepository.findTotalAmountPerProduct())
                .thenReturn(List.of(new Object[]{"Product A", BigInteger.valueOf(10000)},
                        new Object[]{"Product A", BigInteger.valueOf(5000)}));

        Map<String, BigInteger> result = invoiceService.getTotalAmountPerProduct();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BigInteger.valueOf(15000), result.get("Product A"));
    }

    @Test
     void testFindAllFiltered_NoFilters() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "invoiceDate"));
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());

        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice));

        when(invoiceRepository.findAll(pageable)).thenReturn(invoicePage);

        Page<Invoice> result = invoiceService.findAllFiltered(0, 10, "invoiceDate", "ASC", null, null, null);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(invoiceRepository, times(1)).findAll(pageable);
    }

    @Test
    void testUpdate_InvoiceTooOld() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setCreatedTime(LocalDateTime.now().minusMinutes(15));

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        assertThrows(IllegalArgumentException.class, () -> invokeUpdate(invoiceId, invoice));
    }

    private void invokeUpdate(UUID invoiceId, Invoice invoice) {
        invoiceService.update(invoiceId, invoice, new ArrayList<>());
    }


    @Test
     void testUpdate_RemoveOldOrderItems() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);

        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setPrice(BigInteger.valueOf(1000));
        product.setQuantity(10);

        OrderItem oldOrderItem = new OrderItem();
        oldOrderItem.setProduct(product);
        oldOrderItem.setQuantity(2);
        oldOrderItem.setAmount(BigInteger.valueOf(2000));

        invoice.setListOrderItem(new ArrayList<>(List.of(oldOrderItem)));

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(productService.findById(any(UUID.class))).thenReturn(product);

        Invoice updatedInvoice = invoiceService.update(invoiceId, invoice, new ArrayList<>());

        assertNotNull(updatedInvoice);
        assertTrue(updatedInvoice.getListOrderItem().isEmpty());
        verify(productRepository, times(1)).save(product);
        verify(orderItemRepository, times(1)).saveAll(anyList());
    }


    @Test
     void testCreateInvoice_CustomerNotActive() {
        UUID productId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(BigInteger.valueOf(500));
        product.setQuantity(10);
        product.setActive(true);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setActive(false);

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);

        when(productService.findById(productId)).thenReturn(product);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        assertThrows(IllegalArgumentException.class, () -> invokeCreateInvoice(invoice, orderItem));
    }

    @Test
     void testGetTotalAmountPerDay_NoInvoices() {
        LocalDate date = LocalDate.now();

        when(invoiceRepository.findTotalAmountByDate(date)).thenReturn(BigInteger.ZERO);

        BigInteger result = invoiceService.getTotalAmountPerDay(date);

        assertNotNull(result);
        assertEquals(BigInteger.ZERO, result);
    }

    @Test
     void testFindAllByDate_NoInvoicesFound() {
        LocalDate date = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "invoiceDate"));

        when(invoiceRepository.findByInvoiceDate(date, pageable)).thenReturn(Page.empty());

        Page<Invoice> result = invoiceService.findAllByDate(0, 10, date, "ASC");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(invoiceRepository, times(1)).findByInvoiceDate(date, pageable);
    }

    @Test
     void testFindAllByMonth_NoInvoicesFound() {
        int month = 8;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "invoiceDate"));

        when(invoiceRepository.findByMonth(month, pageable)).thenReturn(Page.empty());

        Page<Invoice> result = invoiceService.findAllByMonth(0, 10, month, "ASC");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(invoiceRepository, times(1)).findByMonth(month, pageable);
    }

    @Test
     void testGenerateToPdf_NoOrderItems() throws IOException {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setListOrderItem(new ArrayList<>());

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(documentUtils.generateByteInvoice(invoice)).thenReturn(new byte[]{1, 2, 3});

        byte[] pdfBytes = invoiceService.generateToPdf(invoiceId);

        assertNotNull(pdfBytes);
        verify(documentUtils, times(1)).generateByteInvoice(invoice);
    }

    @Test
     void testGetTop3ProductsByAmount_LessThan3Products() {
        Object[] productData1 = new Object[]{"Product A", BigInteger.valueOf(5000)};
        Object[] productData2 = new Object[]{"Product B", BigInteger.valueOf(3000)};
        List<Object[]> mockData = Arrays.asList(productData1, productData2);

        when(orderItemRepository.findTopProductsByAmount()).thenReturn(mockData);

        List<Map<String, Object>> result = invoiceService.getTop3ProductsByAmount();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product A", result.get(0).get("name"));
        assertEquals(BigInteger.valueOf(5000), result.get(0).get("totalAmount"));
        assertEquals("Product B", result.get(1).get("name"));
        assertEquals(BigInteger.valueOf(3000), result.get(1).get("totalAmount"));
    }

    @Test
     void testGetInvoicesByFilter_AllNull() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());

        when(invoiceRepository.findAll()).thenReturn(List.of(invoice));

        List<Invoice> result = invoiceService.getInvoicesByFilter(null, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
     void testSave_WithCreatedTimeAndUpdatedTime() {
        OrderItem orderItem = new OrderItem();
        orderItem.setAmount(BigInteger.valueOf(1000));
        Invoice invoice = new Invoice();
        invoice.setListOrderItem(List.of(orderItem));
        invoice.setCreatedTime(LocalDateTime.now().minusDays(1));
        invoice.setUpdatedTime(LocalDateTime.now().minusDays(1));

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        Invoice savedInvoice = invoiceService.save(invoice);

        assertNotNull(savedInvoice);
        assertEquals(BigInteger.valueOf(1000), savedInvoice.getTotalAmount());
        assertNotNull(savedInvoice.getCreatedTime());
        assertNotNull(savedInvoice.getUpdatedTime());
    }

    @Test
     void testFindAllSorted_DescendingOrder() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "invoiceDate"));
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());
        invoice.setListOrderItem(new ArrayList<>());

        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice));

        when(invoiceRepository.findAll(pageable)).thenReturn(invoicePage);

        Page<Invoice> result = invoiceService.findAllSorted(0, 10, "invoiceDate", "DESC");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(invoiceRepository, times(1)).findAll(pageable);
    }


    @Test
     void testUpdate_DifferentOrderItems() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);

        Product oldProduct = new Product();
        oldProduct.setProductId(UUID.randomUUID());
        oldProduct.setPrice(BigInteger.valueOf(1000));
        oldProduct.setQuantity(10);
        oldProduct.setActive(true);

        OrderItem oldOrderItem = new OrderItem();
        oldOrderItem.setProduct(oldProduct);
        oldOrderItem.setQuantity(2);
        oldOrderItem.setAmount(BigInteger.valueOf(2000));

        Product newProduct = new Product();
        newProduct.setProductId(UUID.randomUUID());
        newProduct.setPrice(BigInteger.valueOf(2000));
        newProduct.setQuantity(20);
        newProduct.setActive(true);

        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setProduct(newProduct);
        newOrderItem.setQuantity(3);
        newOrderItem.setAmount(BigInteger.valueOf(6000));

        invoice.setListOrderItem(new ArrayList<>(List.of(oldOrderItem)));

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(productService.findById(newProduct.getProductId())).thenReturn(newProduct);
        when(productService.findById(oldProduct.getProductId())).thenReturn(oldProduct);

        Invoice updatedInvoice = invoiceService.update(invoiceId, invoice, List.of(newOrderItem));

        assertNotNull(updatedInvoice);
        assertEquals(1, updatedInvoice.getListOrderItem().size());
        assertEquals(newProduct.getProductId(), updatedInvoice.getListOrderItem().get(0).getProduct().getProductId());
        verify(productRepository, times(1)).save(newProduct);
        verify(productRepository, times(1)).save(oldProduct);
    }


    @Test
     void testCreateInvoice_InsufficientQuantityOnAvailableProduct() {
        UUID productId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(BigInteger.valueOf(500));
        product.setQuantity(1);
        product.setActive(true);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setActive(true);

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);

        when(productService.findById(productId)).thenReturn(product);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        assertThrows(IllegalArgumentException.class, () -> invokeCreateInvoice(invoice, orderItem));
    }

    @Test
     void testGetTotalAmountPerYear_NoData() {
        int year = 2024;

        when(invoiceRepository.findTotalAmountByYear(year)).thenReturn(BigInteger.ZERO);

        BigInteger result = invoiceService.getTotalAmountPerYear(year);

        assertNotNull(result);
        assertEquals(BigInteger.ZERO, result);
    }

    @Test
    void testUpdate_ProductBecomesInactiveAfterUpdate() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);

        Customer customer = new Customer();
        customer.setCustomerId(UUID.randomUUID());
        invoice.setCustomer(customer);

        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setPrice(BigInteger.valueOf(1000));
        product.setQuantity(10);
        product.setActive(true);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItem.setAmount(BigInteger.valueOf(2000));

        invoice.setListOrderItem(new ArrayList<>(List.of(orderItem)));

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(productService.findById(any(UUID.class))).thenReturn(product);

        product.setActive(false);

        assertThrows(IllegalArgumentException.class, () -> invokeCreateInvoice(invoice, orderItem));
    }

    @Test
     void testGenerateToPdf_EmptyInvoice() throws IOException {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setListOrderItem(new ArrayList<>());

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(documentUtils.generateByteInvoice(invoice)).thenReturn(new byte[]{});

        byte[] pdfBytes = invoiceService.generateToPdf(invoiceId);

        assertNotNull(pdfBytes);
        assertEquals(0, pdfBytes.length);
    }

    @Test
     void testFindAllByCustomerName_CustomerNotFound() {
        String customerName = "Non Existent";
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "invoiceDate"));

        when(invoiceRepository.findAllByCustomerName(customerName, pageable)).thenReturn(Page.empty());

        Page<Invoice> result = invoiceService.findAllByCustomerName(0, 10, "invoiceDate", "ASC", customerName);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(invoiceRepository, times(1)).findAllByCustomerName(customerName, pageable);
    }

    @Test
     void testSave_NoOrderItems() {
        Invoice invoice = new Invoice();
        invoice.setListOrderItem(new ArrayList<>());

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        Invoice savedInvoice = invoiceService.save(invoice);

        assertNotNull(savedInvoice);
        assertEquals(BigInteger.ZERO, savedInvoice.getTotalAmount());
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
     void testCreateInvoice_ProductBecomesInactiveAfterOrder() {
        UUID productId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(BigInteger.valueOf(500));
        product.setQuantity(10);
        product.setActive(true);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setActive(true);

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);

        when(productService.findById(productId)).thenReturn(product);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        product.setActive(false);

        assertThrows(IllegalArgumentException.class, () -> invokeCreateInvoice(invoice, orderItem));
    }

    @Test
    void testUpdate_NewOrderItemWithInactiveProduct() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);

        Product oldProduct = new Product();
        oldProduct.setProductId(UUID.randomUUID());
        oldProduct.setPrice(BigInteger.valueOf(1000));
        oldProduct.setQuantity(10);
        oldProduct.setActive(true);

        OrderItem oldOrderItem = new OrderItem();
        oldOrderItem.setProduct(oldProduct);
        oldOrderItem.setQuantity(2);
        oldOrderItem.setAmount(BigInteger.valueOf(2000));

        Product newProduct = new Product();
        newProduct.setProductId(UUID.randomUUID());
        newProduct.setPrice(BigInteger.valueOf(2000));
        newProduct.setQuantity(20);
        newProduct.setActive(false);

        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setProduct(newProduct);
        newOrderItem.setQuantity(3);
        newOrderItem.setAmount(BigInteger.valueOf(6000));

        invoice.setListOrderItem(new ArrayList<>(List.of(oldOrderItem)));

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(productService.findById(newProduct.getProductId())).thenReturn(newProduct);

        assertThrows(IllegalArgumentException.class, () -> invokeUpdateWithNewOrderItem(invoiceId, invoice, newOrderItem));
    }

    private void invokeUpdateWithNewOrderItem(UUID invoiceId, Invoice invoice, OrderItem newOrderItem) {
        invoiceService.update(invoiceId, invoice, List.of(newOrderItem));
    }


    @Test
     void testGetTotalAmountPerDay_WithData() {
        LocalDate date = LocalDate.now();
        BigInteger totalAmount = BigInteger.valueOf(10000);

        when(invoiceRepository.findTotalAmountByDate(date)).thenReturn(totalAmount);

        BigInteger result = invoiceService.getTotalAmountPerDay(date);

        assertNotNull(result);
        assertEquals(totalAmount, result);
        verify(invoiceRepository, times(1)).findTotalAmountByDate(date);
    }

    @Test
     void testGetTotalAmountPerMonth_WithMultipleData() {
        int month = 8;
        int year = 2024;
        BigInteger totalAmount = BigInteger.valueOf(30000);

        when(invoiceRepository.findTotalAmountByMonth(month, year)).thenReturn(totalAmount);

        BigInteger result = invoiceService.getTotalAmountPerMonth(month, year);

        assertNotNull(result);
        assertEquals(totalAmount, result);
        verify(invoiceRepository, times(1)).findTotalAmountByMonth(month, year);
    }

    @Test
     void testGetSoldProducts_NoProductsSold() {
        when(orderItemRepository.findSoldProducts()).thenReturn(new ArrayList<>());

        List<String> result = invoiceService.getSoldProducts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderItemRepository, times(1)).findSoldProducts();
    }

    @Test
     void testFindAllByDate_FutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "invoiceDate"));

        when(invoiceRepository.findByInvoiceDate(futureDate, pageable)).thenReturn(Page.empty());

        Page<Invoice> result = invoiceService.findAllByDate(0, 10, futureDate, "ASC");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(invoiceRepository, times(1)).findByInvoiceDate(futureDate, pageable);
    }

    @Test
     void testFindAllByCustomerName_SpecialCharacters() {
        String customerName = "John@Doe#2024";
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "invoiceDate"));
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(UUID.randomUUID());

        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice));

        when(invoiceRepository.findAllByCustomerName(customerName, pageable)).thenReturn(invoicePage);

        Page<Invoice> result = invoiceService.findAllByCustomerName(0, 10, "invoiceDate", "ASC", customerName);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(invoiceRepository, times(1)).findAllByCustomerName(customerName, pageable);
    }

    @Test
     void testGenerateToPdf_DocumentUtilsReturnsNull() throws IOException {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(documentUtils.generateByteInvoice(invoice)).thenReturn(null);

        byte[] pdfBytes = invoiceService.generateToPdf(invoiceId);

        assertNull(pdfBytes);
        verify(documentUtils, times(1)).generateByteInvoice(invoice);
    }

    @Test
     void testGetTotalAmountPerYear_FutureYear() {
        int futureYear = LocalDate.now().getYear() + 5;
        BigInteger totalAmount = BigInteger.valueOf(0);

        when(invoiceRepository.findTotalAmountByYear(futureYear)).thenReturn(totalAmount);

        BigInteger result = invoiceService.getTotalAmountPerYear(futureYear);

        assertNotNull(result);
        assertEquals(BigInteger.ZERO, result);
        verify(invoiceRepository, times(1)).findTotalAmountByYear(futureYear);
    }

    @Test
     void testFindAllByMonth_NoInvoicesForMonth() {
        int month = 12;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "invoiceDate"));

        when(invoiceRepository.findByMonth(month, pageable)).thenReturn(Page.empty());

        Page<Invoice> result = invoiceService.findAllByMonth(0, 10, month, "ASC");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(invoiceRepository, times(1)).findByMonth(month, pageable);
    }

}
