package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.motoCare.domain.VehicleProductEntity;

public interface VehicleProductRepository extends JpaRepository<VehicleProductEntity, Long>, JpaSpecificationExecutor<VehicleProductEntity> {
    boolean existsByBrandAndModelAndName(String brand, String model, String name);
    boolean existsByBrandAndModelAndNameAndIdNot(String brand, String model, String name, Long id);
}
