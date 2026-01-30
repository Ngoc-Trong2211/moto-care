package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.motoCare.domain.MaintenanceEntity;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<MaintenanceEntity, Long>, JpaSpecificationExecutor<MaintenanceEntity> {
    List<MaintenanceEntity> findByVehicleId(Long vehicleId);
}
