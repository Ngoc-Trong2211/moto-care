package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.motoCare.domain.VehicleProductEntity;

public interface VehicleProductRepository extends JpaRepository<VehicleProductEntity, Long> {
}
