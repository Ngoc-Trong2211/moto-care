package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.motoCare.domain.VehicleEntity;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
}
