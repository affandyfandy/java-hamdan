package lecture16.invoice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import lecture16.invoice.client.ProductClient;
import lecture16.invoice.dto.InvoiceDTO;
import lecture16.invoice.dto.ProductDTO;
import lecture16.invoice.model.Invoice;
import lecture16.invoice.repository.InvoiceRepository;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ProductClient productClient;

    private static final String PRODUCT_SERVICE_URL = "http://localhost:8081/products";

    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(invoice -> convertToDTO(invoice, ClientType.REST_TEMPLATE))
                .collect(Collectors.toList());
    }

    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        return convertToDTO(invoice, ClientType.REST_TEMPLATE);
    }

    public InvoiceDTO saveInvoice(Invoice invoice) {
        return convertToDTO(invoiceRepository.save(invoice), ClientType.REST_TEMPLATE);
    }

    public List<InvoiceDTO> getAllInvoicesFeign() {
        return invoiceRepository.findAll().stream()
                .map(invoice -> convertToDTO(invoice, ClientType.FEIGN_CLIENT))
                .collect(Collectors.toList());
    }

    public InvoiceDTO getInvoiceByIdFeign(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        return convertToDTO(invoice, ClientType.FEIGN_CLIENT);
    }

    public InvoiceDTO saveInvoiceFeign(Invoice invoice) {
        return convertToDTO(invoiceRepository.save(invoice), ClientType.FEIGN_CLIENT);
    }

    public List<InvoiceDTO> getAllInvoicesWebClient() {
        return invoiceRepository.findAll().stream()
                .map(invoice -> convertToDTO(invoice, ClientType.WEB_CLIENT))
                .collect(Collectors.toList());
    }

    public InvoiceDTO getInvoiceByIdWebClient(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        return convertToDTO(invoice, ClientType.WEB_CLIENT);
    }

    public InvoiceDTO saveInvoiceWebClient(Invoice invoice) {
        return convertToDTO(invoiceRepository.save(invoice), ClientType.WEB_CLIENT);
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

    public enum ClientType {
        REST_TEMPLATE,
        FEIGN_CLIENT,
        WEB_CLIENT
    }
}
