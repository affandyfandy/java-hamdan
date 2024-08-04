package lecture16.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import lecture16.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
