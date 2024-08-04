package lecture16.invoice.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lecture16.invoice.dto.ProductDTO;

@Service
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String PRODUCT_SERVICE_URL = "http://localhost:8081/products";

    public ProductDTO getProductById(Long id) {
        return restTemplate.getForObject(PRODUCT_SERVICE_URL + "/" + id, ProductDTO.class);
    }

    public List<ProductDTO> getAllProducts() {
        ProductDTO[] products = restTemplate.getForObject(PRODUCT_SERVICE_URL, ProductDTO[].class);
        return Arrays.asList(products);
    }
}
