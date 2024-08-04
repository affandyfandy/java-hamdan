package lecture16.invoice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lecture16.invoice.dto.ProductDTO;

@FeignClient(name = "product-service", url = "http://localhost:8081/products")
public interface ProductClient {
    @GetMapping("/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);
}
