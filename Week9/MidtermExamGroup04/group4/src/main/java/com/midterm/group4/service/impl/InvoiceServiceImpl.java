package com.midterm.group4.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.midterm.group4.data.model.OrderItem;
import com.midterm.group4.data.model.Customer;
import com.midterm.group4.data.repository.OrderItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

import com.midterm.group4.data.model.Invoice;
import com.midterm.group4.data.model.Product;
import com.midterm.group4.data.repository.CustomerRepository;
import com.midterm.group4.data.repository.InvoiceRepository;
import com.midterm.group4.data.repository.ProductRepository;
import com.midterm.group4.exception.ObjectNotFoundException;
import com.midterm.group4.service.InvoiceService;
import com.midterm.group4.service.ProductService;
import com.midterm.group4.utils.DocumentUtils;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final OrderItemRepository orderItemRepository;
    private final DocumentUtils documentUtils;
    private final CustomerRepository customerRepository;

    @Autowired
    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            ProductRepository productRepository,
            ProductService productService,
            OrderItemRepository orderItemRepository,
            DocumentUtils documentUtils,
            CustomerRepository customerRepository) {
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.orderItemRepository = orderItemRepository;
        this.documentUtils = documentUtils;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public Page<Invoice> findAllSorted(int pageNo, int pageSize, String sortBy, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromOptionalString(sortOrder).orElse(Sort.Direction.ASC), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Invoice> invoices = invoiceRepository.findAll(pageable);

        invoices.forEach(invoice -> {
            BigInteger totalAmount = BigInteger.ZERO;
            for (OrderItem item : invoice.getListOrderItem()) {
                totalAmount = totalAmount.add(item.getAmount());
            }
            invoice.setTotalAmount(totalAmount);
        });

        return invoices;
    }

    @Override
    @Transactional
    public Page<Invoice> findAll(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return invoiceRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Page<Invoice> findAllByDate(int pageNo, int pageSize, LocalDate invoiceDate, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromOptionalString(sortOrder).orElse(Sort.Direction.ASC), "invoiceDate");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Invoice> invoices = invoiceRepository.findByInvoiceDate(invoiceDate, pageable);

        if (invoices != null) {
            invoices.forEach(invoice -> {
                BigInteger totalAmount = BigInteger.ZERO;
                for (OrderItem item : invoice.getListOrderItem()) {
                    totalAmount = totalAmount.add(item.getAmount());
                }
                invoice.setTotalAmount(totalAmount);
            });
        }

        return invoices;
    }

    @Override
    @Transactional
    public Page<Invoice> findAllByMonth(int pageNo, int pageSize, int month, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromOptionalString(sortOrder).orElse(Sort.Direction.ASC), "invoiceDate");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Invoice> invoices = invoiceRepository.findByMonth(month, pageable);

        if (invoices != null) {
            invoices.forEach(invoice -> {
                BigInteger totalAmount = BigInteger.ZERO;
                for (OrderItem item : invoice.getListOrderItem()) {
                    totalAmount = totalAmount.add(item.getAmount());
                }
                invoice.setTotalAmount(totalAmount);
            });
        }

        return invoices;
    }


    @Override
    @Transactional
    public Invoice findById(UUID id) {
        return invoiceRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Invoice not found with ID: " + id));
    }

    @Override
    @Transactional
    public Invoice save(Invoice invoice) {
        List<OrderItem> listOrderItem = invoice.getListOrderItem();
        BigInteger totalAmount = BigInteger.ZERO;

        for (OrderItem order : listOrderItem){
            if (order.getAmount() != null) {
                totalAmount = totalAmount.add(order.getAmount());
            }
        }
        invoice.setTotalAmount(totalAmount);
        invoice.setCreatedTime(LocalDateTime.now());
        invoice.setUpdatedTime(LocalDateTime.now());
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice update(UUID id, Invoice invoice, List<OrderItem> listOrderItem) {
        Invoice findInvoice = findById(id);
        if (findInvoice == null) return null;

        LocalDateTime createdTime = findInvoice.getCreatedTime();
        if (createdTime == null) {
            createdTime = LocalDateTime.now(); // DEBUGGING
            findInvoice.setCreatedTime(createdTime);
        }
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(createdTime, currentTime);
        if (duration.toMinutes() > 10) {
            throw new IllegalArgumentException("Invoice can't be edited");
        }

        Map<UUID, OrderItem> currentOrderItemsMap = findInvoice.getListOrderItem().stream()
                .collect(Collectors.toMap(orderItem -> orderItem.getProduct().getProductId(), orderItem -> orderItem));

        BigInteger totalAmount = BigInteger.ZERO;
        List<OrderItem> updatedOrderItems = new ArrayList<>();

        for (OrderItem newOrderItem : listOrderItem) {
            UUID productId = newOrderItem.getProduct().getProductId();
            Product product = productService.findById(productId);

            BigInteger amount = getBigInteger(newOrderItem, product);

            product.setQuantity(product.getQuantity() - newOrderItem.getQuantity());
            productRepository.save(product);

            if (currentOrderItemsMap.containsKey(productId)) {
                OrderItem existingOrderItem = currentOrderItemsMap.get(productId);
                BigInteger oldAmount = existingOrderItem.getAmount();
                existingOrderItem.setQuantity(newOrderItem.getQuantity());
                existingOrderItem.setAmount(amount);
                updatedOrderItems.add(existingOrderItem);

                totalAmount = totalAmount.add(amount).subtract(oldAmount);
            } else {
                newOrderItem.setAmount(amount);
                newOrderItem.setInvoice(findInvoice);
                newOrderItem.setProduct(product);
                updatedOrderItems.add(newOrderItem);

                totalAmount = totalAmount.add(amount);
            }
        }

        List<OrderItem> removedOrderItems = findInvoice.getListOrderItem().stream()
                .filter(orderItem -> listOrderItem.stream()
                        .noneMatch(newOrderItem -> newOrderItem.getProduct().getProductId().equals(orderItem.getProduct().getProductId())))
                .toList();

        for (OrderItem removedOrderItem : removedOrderItems) {
            UUID productId = removedOrderItem.getProduct().getProductId();
            Product product = productService.findById(productId);

            if (product != null) {
                product.setQuantity(product.getQuantity() + removedOrderItem.getQuantity());
                productRepository.save(product);
            }

            totalAmount = totalAmount.subtract(removedOrderItem.getAmount());
        }

        findInvoice.setTotalAmount(totalAmount);
        findInvoice.getListOrderItem().clear();
        findInvoice.getListOrderItem().addAll(updatedOrderItems);

        invoiceRepository.save(findInvoice);
        orderItemRepository.saveAll(updatedOrderItems);

        return findInvoice;
    }

    private static BigInteger getBigInteger(OrderItem newOrderItem, Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product does not exist");
        }
        if (!product.isActive()) {
            throw new IllegalArgumentException("Product is not active");
        }
        if (product.getQuantity() < newOrderItem.getQuantity()) {
            throw new IllegalArgumentException("Insufficient product quantity");
        }

        BigInteger quantity = BigInteger.valueOf(newOrderItem.getQuantity());
        BigInteger price = product.getPrice();
        return price.multiply(quantity);
    }


    @Override
    @Transactional
    public byte[] generateToPdf(UUID id) throws IOException {
        Invoice invoice = findById(id);
        if (invoice != null){
            return documentUtils.generateByteInvoice(invoice);
        }
        return new byte[0];
    }

    @Override
    @Transactional
    public Page<Invoice> findAllFiltered(int pageNo, int pageSize, String sortBy, String sortOrder, UUID customerId, String invoiceDate, String month) {
        Sort.Direction direction = getSortDirection(sortOrder);
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));

        Page<Invoice> invoices = findInvoices(customerId, invoiceDate, month, pageable);
        return calculateTotalAmounts(invoices);
    }

    private Sort.Direction getSortDirection(String sortOrder) {
        return "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    private Page<Invoice> findInvoices(UUID customerId, String invoiceDate, String month, Pageable pageable) {
        if (customerId != null && invoiceDate != null && month != null) {
            return invoiceRepository.findAllFiltered(customerId, invoiceDate, month, pageable);
        }
        if (customerId != null && invoiceDate != null) {
            return invoiceRepository.findAllByCustomerIdAndInvoiceDate(customerId, invoiceDate, pageable);
        }
        if (customerId != null && month != null) {
            return invoiceRepository.findAllByCustomerIdAndMonth(customerId, month, pageable);
        }
        if (invoiceDate != null && month != null) {
            return invoiceRepository.findAllByInvoiceDateAndMonth(invoiceDate, month, pageable);
        }
        if (customerId != null) {
            return invoiceRepository.findAllByCustomerId(customerId, pageable);
        }
        if (invoiceDate != null) {
            return invoiceRepository.findAllByInvoiceDate(invoiceDate, pageable);
        }
        if (month != null) {
            return invoiceRepository.findAllByMonth(month, pageable);
        }
        return invoiceRepository.findAll(pageable);  // Handle case with no filters
    }

    private Page<Invoice> calculateTotalAmounts(Page<Invoice> invoices) {
        if (invoices == null) {
            return Page.empty();  // Ensure invoices is never null
        }

        invoices.forEach(invoice -> {
            if (invoice.getListOrderItem() == null) {
                invoice.setListOrderItem(new ArrayList<>());  // Initialize if null
            }
            BigInteger totalAmount = invoice.getListOrderItem().stream()
                    .map(OrderItem::getAmount)
                    .reduce(BigInteger.ZERO, BigInteger::add);
            invoice.setTotalAmount(totalAmount);
        });

        return invoices;
    }




    @Override
    @Transactional
    public Invoice createInvoice(Invoice invoice, List<OrderItem> listOrderItem) {
        if (invoice.getListOrderItem() == null){
            invoice.setListOrderItem(new ArrayList<>());
        }

        List<OrderItem> newOrderItems = new ArrayList<>();

        Optional<Customer> cust = customerRepository.findById(invoice.getCustomer().getCustomerId());

        Customer customer;
        if (cust.isPresent()){
            customer = cust.get();
            if (!customer.isActive()) throw new IllegalArgumentException("Customer is deactive");
        }
        else {
            throw new IllegalArgumentException("Customer doesn't exist");
        }

        BigInteger totalAmount = BigInteger.ZERO;

        for (OrderItem orderItem : listOrderItem){

            Product product = productService.findById(orderItem.getProduct().getProductId());
            if (!product.isActive()) {
                throw new IllegalArgumentException("Product is not active");
            }
            else if (product.getQuantity() < orderItem.getQuantity()){
                throw new IllegalArgumentException("Insuffient product quantity");
            }

            BigInteger qty = BigInteger.valueOf(orderItem.getQuantity());
            BigInteger price = product.getPrice();
            BigInteger amount = price.multiply(qty);
            orderItem.setAmount(amount);

            orderItem.setInvoice(invoice);
            orderItem.setProduct(product);

            Integer qtyRemain = product.getQuantity() - orderItem.getQuantity();
            product.setQuantity(qtyRemain);

            newOrderItems.add(orderItem);

            totalAmount = totalAmount.add(amount);

            productRepository.save(product);
        }

        orderItemRepository.saveAll(newOrderItems);

        invoice.setListOrderItem(newOrderItems);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setCustomer(customer);
        invoice.setTotalAmount(totalAmount);

        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Page<Invoice> findAllByCustomerName(int pageNo, int pageSize, String sortBy, String sortOrder, String name) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return invoiceRepository.findAllByCustomerName(name, pageable);
    }

    @Override
    @Transactional
    public BigInteger getTotalAmountPerDay(LocalDate date) {
        return invoiceRepository.findTotalAmountByDate(date);
    }

    @Override
    @Transactional
    public BigInteger getTotalAmountPerMonth(int month, int year) {
        return invoiceRepository.findTotalAmountByMonth(month, year);
    }

    @Override
    @Transactional
    public BigInteger getTotalAmountPerYear(int year) {
        return invoiceRepository.findTotalAmountByYear(year);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getTop3ProductsByAmount() {
        return orderItemRepository.findTopProductsByAmount().stream()
                .map(data -> Map.of("name", data[0], "totalAmount", data[1]))
                .limit(3)
                .toList();
    }

    @Override
    @Transactional
    public List<String> getSoldProducts() {
        return orderItemRepository.findSoldProducts();
    }

    @Override
    @Transactional
    public Map<String, Long> getTotalQuantityPerProduct() {
        return orderItemRepository.findTotalQuantityPerProduct()
                .stream().collect(Collectors.toMap(
                        data -> (String) data[0],
                        data -> ((Number) data[1]).longValue()
                ));
    }

    @Override
    @Transactional
    public Map<String, BigInteger> getTotalAmountPerProduct() {
        return orderItemRepository.findTotalAmountPerProduct()
                .stream().collect(Collectors.toMap(
                        data -> (String) data[0],
                        data -> (BigInteger) data[1],
                        BigInteger::add
                ));
    }

    @Override
    @Transactional
    public List<Invoice> getInvoicesByFilter(UUID customerId, Integer month, Integer year) {
        if (customerId != null && month != null && year != null) {
            return invoiceRepository.findByCustomerAndMonthAndYear(customerId, month, year);
        } else if (customerId != null && month != null) {
            return invoiceRepository.findByCustomerAndMonth(customerId, month);
        } else if (customerId != null && year != null) {
            return invoiceRepository.findByCustomerAndYear(customerId, year);
        } else if (month != null && year != null) {
            return invoiceRepository.findByMonthAndYear(month, year);
        } else if (customerId != null) {
            return invoiceRepository.findByCustomer(customerId);
        } else if (year != null) {
            return invoiceRepository.findByYear(year);
        } else {
            return invoiceRepository.findAll();
        }
    }
}
