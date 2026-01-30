package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.motoCare.domain.MaintenanceReminderEntity;

import java.util.List;

public interface MaintenanceReminderRepository extends JpaRepository<MaintenanceReminderEntity, Long> {
    List<MaintenanceReminderEntity> findByVehicleId(Long vehicleId);
}
