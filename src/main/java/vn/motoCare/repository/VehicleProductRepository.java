package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.motoCare.domain.VehicleProductEntity;

public interface VehicleProductRepository extends JpaRepository<VehicleProductEntity, Long> {
    boolean existsByBrandAndModelAndName(String brand, String model, String name);
    boolean existsByBrandAndModelAndNameAndIdNot(String brand, String model, String name, Long id);
}
