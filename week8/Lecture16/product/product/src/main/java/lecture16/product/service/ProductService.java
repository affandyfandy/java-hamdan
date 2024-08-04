package lecture16.product.service;

import lecture16.product.dto.ProductDTO;
import lecture16.product.model.Product;
import lecture16.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    public ProductDTO saveProduct(Product product) {
        return convertToDTO(productRepository.save(product));
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getProductId());
        productDTO.setName(product.getProductName());
        return productDTO;
    }
}
