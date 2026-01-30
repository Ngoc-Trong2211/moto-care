package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.motoCare.domain.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
