package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.motoCare.domain.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
