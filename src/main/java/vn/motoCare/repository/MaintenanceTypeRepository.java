package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.motoCare.domain.MaintenanceTypeEntity;

public interface MaintenanceTypeRepository extends JpaRepository<MaintenanceTypeEntity, Long> {
}
