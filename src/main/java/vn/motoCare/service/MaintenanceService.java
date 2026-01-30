package vn.motoCare.service;

import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.request.maintenance.CreateMaintenanceRequest;
import vn.motoCare.domain.request.maintenance.MaintenanceSpecificationRequest;
import vn.motoCare.domain.request.maintenance.UpdateMaintenanceRequest;
import vn.motoCare.domain.response.maintenance.CreateMaintenanceResponse;
import vn.motoCare.domain.response.maintenance.GetMaintenanceResponse;
import vn.motoCare.domain.response.maintenance.UpdateMaintenanceResponse;

public interface MaintenanceService {
    CreateMaintenanceResponse handleCreate(CreateMaintenanceRequest request);
    CreateMaintenanceResponse handleGetById(Long id);
    GetMaintenanceResponse handleGetByVehicleId(Long vehicleId);
    GetMaintenanceResponse handleGetMaintenances(Pageable pageable, MaintenanceSpecificationRequest req);
    UpdateMaintenanceResponse handleUpdate(UpdateMaintenanceRequest request);
    void handleDelete(Long id);
}
