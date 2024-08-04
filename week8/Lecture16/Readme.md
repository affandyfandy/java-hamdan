
# Lecture16: Assignment1 Feign Client, Rest Template, Web Client

In this assignment we will implement Feign Client, Rest Template, Web Client to connect 2 projects (Invoice and product).

## FeignClient

**FeignClient** is a declarative HTTP client provided by Spring Cloud. Feign makes it easier to write web service clients by using annotations. You define an interface and annotate it, and Feign handles the HTTP requests and responses for you, including data conversion.

## RestTemplate

**RestTemplate** is a synchronous client to perform HTTP requests in a Spring application. It provides methods for interacting with RESTful services, such as GET, POST, PUT, DELETE, etc. It is the traditional approach in Spring for making HTTP calls.

## WebClient

**WebClient** is part of Spring WebFlux and is the successor of RestTemplate for reactive applications. WebClient supports both synchronous and asynchronous operations in a non-blocking manner. It provides more flexibility and power compared to RestTemplate, especially in the context of reactive programming.

## Comparison Table

| **Aspect**         | **FeignClient**                                            | **RestTemplate**                                    | **WebClient**                                    |
|--------------------|------------------------------------------------------------|-----------------------------------------------------|--------------------------------------------------|
| **Introduced in**  | Spring Cloud                                               | Spring Framework                                    | Spring WebFlux                                   |
| **Nature**         | Declarative Client                                         | Template-based Client                               | Functional, Non-blocking Client                  |
| **Synchronous**    | Yes                                                        | Yes                                                 | Yes                                              |
| **Asynchronous**   | No                                                         | No                                                  | Yes                                              |
| **Ease of Use**    | High (uses annotations and interface)                      | Medium (uses methods to create requests)            | Medium (functional API with builder pattern)     |
| **Configuration**  | Easy (minimal configuration, uses annotations)             | Manual (more boilerplate code for configurations)   | Medium (requires setup, powerful configurations) |
| **Flexibility**    | Limited to annotations                                     | High (full control over HTTP requests)              | High (supports reactive streams, flexible)       |
| **Reactive Support** | No                                                        | No                                                  | Yes                                              |

## Implementation in the Project

In this, we will implement these clients to fetch product data from the `product-service` in different ways.

### FeignClient Implementation

1. **Add FeignClient dependency in `pom.xml`:**

   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-openfeign</artifactId>
   </dependency>
   ```

2. **Enable FeignClients in your application:**

   ```java
   @SpringBootApplication
   @EnableFeignClients
   public class InvoiceApplication {
       public static void main(String[] args) {
           SpringApplication.run(InvoiceApplication.class, args);
       }
   }
   ```

3. **Define FeignClient interface:**

    ```java
   @FeignClient(name = "product-service", url = "http://localhost:8081/products")
   public interface ProductClient {
       @GetMapping("/{id}")
       ProductDTO getProductById(@PathVariable("id") Long id);
   }
    ```

4. **Use FeignClient in your service:**

   ```java
   @Service
   public class InvoiceService {
       @Autowired
       private ProductClient productClient;

       public ProductDTO getProductByIdUsingFeign(Long id) {
           return productClient.getProductById(id);
       }
   }
   ```

### RestTemplate Implementation

1. **Add RestTemplate bean in your application:**
```java
   @SpringBootApplication
   public class InvoiceApplication {
       public static void main(String[] args) {
           SpringApplication.run(InvoiceApplication.class, args);
       }

       @Bean
       public RestTemplate restTemplate() {
           return new RestTemplate();
       }
   }
```

2. **Use RestTemplate in your service:**
```java
   @Service
   public class InvoiceService {
       @Autowired
       private RestTemplate restTemplate;

       private static final String PRODUCT_SERVICE_URL = "http://localhost:8081/products";

       public ProductDTO getProductByIdUsingRestTemplate(Long id) {
           return restTemplate.getForObject(PRODUCT_SERVICE_URL + "/" + id, ProductDTO.class);
       }
   }
   ```

### WebClient Implementation

1. **Add WebClient dependency in `pom.xml`:**

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-webflux</artifactId>
   </dependency>
   ```

2. **Add WebClient bean in your application:**
```java
   @SpringBootApplication
   public class InvoiceApplication {
       public static void main(String[] args) {
           SpringApplication.run(InvoiceApplication.class, args);
       }

       @Bean
       public WebClient.Builder webClientBuilder() {
           return WebClient.builder();
       }
   }
```
3. **Use WebClient in your service:**
```java
   @Service
   public class InvoiceService {
       @Autowired
       private WebClient.Builder webClientBuilder;

       private static final String PRODUCT_SERVICE_URL = "http://localhost:8081/products";

       public ProductDTO getProductByIdUsingWebClient(Long id) {
           return webClientBuilder.build()
               .get()
               .uri(PRODUCT_SERVICE_URL + "/" + id)
               .retrieve()
               .bodyToMono(ProductDTO.class)
               .block();
       }
   }
```
### Combining in a Single Service

Here is the complete `InvoiceService` combining all three methods:

**InvoiceService.java**
```java
@Service
public class InvoiceService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ProductClient productClient;

    private static final String PRODUCT_SERVICE_URL = "http://localhost:8081/products";

    public enum ClientType {
        REST_TEMPLATE,
        FEIGN_CLIENT,
        WEB_CLIENT
    }

    public List<InvoiceDTO> getAllInvoices(ClientType clientType) {
        return invoiceRepository.findAll().stream()
                .map(invoice -> convertToDTO(invoice, clientType))
                .collect(Collectors.toList());
    }

    public InvoiceDTO getInvoiceById(Long id, ClientType clientType) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        return convertToDTO(invoice, clientType);
    }

    public InvoiceDTO saveInvoice(Invoice invoice) {
        return convertToDTO(invoiceRepository.save(invoice), ClientType.REST_TEMPLATE); // default to RestTemplate
    }

    private InvoiceDTO convertToDTO(Invoice invoice, ClientType clientType) {
        if (invoice == null) {
            return null;
        }
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(invoice.getInvoiceId());

        List<ProductDTO> productDTOs = invoice.getProductIds().stream()
                .map(productId -> fetchProductById(productId, clientType))
                .collect(Collectors.toList());

        invoiceDTO.setProducts(productDTOs);
        return invoiceDTO;
    }

    private ProductDTO fetchProductById(Long productId, ClientType clientType) {
        switch (clientType) {
            case REST_TEMPLATE:
                return restTemplate.getForObject(PRODUCT_SERVICE_URL + "/" + productId, ProductDTO.class);
            case FEIGN_CLIENT:
                return productClient.getProductById(productId);
            case WEB_CLIENT:
                return webClientBuilder.build()
                        .get()
                        .uri(PRODUCT_SERVICE_URL + "/" + productId)
                        .retrieve()
                        .bodyToMono(ProductDTO.class)
                        .block();
            default:
                throw new IllegalArgumentException("Invalid client type");
        }
    }
}
```
**InvoiceController.java**
```java
@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // Default to using REST_TEMPLATE for the standard endpoints
    @GetMapping
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceService.getAllInvoices(ClientType.REST_TEMPLATE);
    }

    @GetMapping("/{id}")
    public InvoiceDTO getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id, ClientType.REST_TEMPLATE);
    }

    @PostMapping
    public InvoiceDTO saveInvoice(@RequestBody Invoice invoice) {
        return invoiceService.saveInvoice(invoice);
    }

    // Feign client specific endpoints
    @GetMapping("/feign")
    public List<InvoiceDTO> getAllInvoicesFeign() {
        return invoiceService.getAllInvoices(ClientType.FEIGN_CLIENT);
    }

    @GetMapping("/feign/{id}")
    public InvoiceDTO getInvoiceByIdFeign(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id, ClientType.FEIGN_CLIENT);
    }

    @PostMapping("/feign")
    public InvoiceDTO saveInvoiceFeign(@RequestBody Invoice invoice) {
        return invoiceService.saveInvoice(invoice, ClientType.FEIGN_CLIENT);
    }

    // WebClient specific endpoints
    @GetMapping("/webclient")
    public List<InvoiceDTO> getAllInvoicesWebClient() {
        return invoiceService.getAllInvoices(ClientType.WEB_CLIENT);
    }

    @GetMapping("/webclient/{id}")
    public InvoiceDTO getInvoiceByIdWebClient(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id, ClientType.WEB_CLIENT);
    }

    @PostMapping("/webclient")
    public InvoiceDTO saveInvoiceWebClient(@RequestBody Invoice invoice) {
        return invoiceService.saveInvoice(invoice, ClientType.WEB_CLIENT);
    }
}
```

## Folder Structure

### Product
```
lecture16.product
├── controller
│   └── ProductController.java
├── dto
│   └── ProductDTO.java
├── model
│   └── Product.java
├── repository
│   └── ProductRepository.java
├── service
│   └── ProductService.java
└── ProductApplication.java
```

## Invoice
```
lecture16.invoice
├── client
│   └── ProductClient.java
├── controller
│   └── InvoiceController.java
├── dto
│   └── InvoiceDTO.java
│   └── ProductDTO.java
├── model
│   └── Invoice.java
├── repository
│   └── InvoiceRepository.java
├── service
│   └── InvoiceService.java
└── InvoiceApplication.java
```

## Mysql Script

### Product
```sql
-- Create the product_db database
CREATE DATABASE product_db;

-- Use the product_db database
USE product_db;

-- Create the product table
CREATE TABLE product (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL
);

-- Insert mock data into the product table
INSERT INTO product (product_name) VALUES ('Product A'), ('Product B'), ('Product C'), ('Product D'), ('Product E');
```

### Invoice
```sql
-- Create the invoice_db database
CREATE DATABASE invoice_db;

-- Use the invoice_db database
USE invoice_db;

-- Create the invoice table
CREATE TABLE invoice (
    invoice_id BIGINT AUTO_INCREMENT PRIMARY KEY
);

-- Create the invoice_product table to store the relationship between invoices and products
CREATE TABLE invoice_product (
    invoice_id BIGINT,
    product_id BIGINT,
    FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id),
    FOREIGN KEY (product_id) REFERENCES product_db.product(product_id)
);
```

For the invoice data, you need to insert it with postman

## Postman Collection

You can use the postman collection provided

### Product Service (running on port 8081)

1. **GET /products**
2. **GET /products/{id}**
3. **POST /products**

### Invoice Service (running on port 8080)

1. **GET /invoices**
2. **GET /invoices/{id}**
3. **POST /invoices**
4. **GET /invoices/feign**
5. **GET /invoices/feign/{id}**
6. **POST /invoices/feign**
7. **GET /invoices/webclient**
8. **GET /invoices/webclient/{id}**
9. **POST /invoices/webclient**
