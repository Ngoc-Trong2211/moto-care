package vn.motoCare.service;

import vn.motoCare.domain.request.maintenanceType.CreateMaintenanceTypeRequest;
import vn.motoCare.domain.request.maintenanceType.UpdateMaintenanceTypeRequest;
import vn.motoCare.domain.response.maintenanceType.CreateMaintenanceTypeResponse;
import vn.motoCare.domain.response.maintenanceType.GetMaintenanceTypeResponse;
import vn.motoCare.domain.response.maintenanceType.UpdateMaintenanceTypeResponse;

public interface MaintenanceTypeService {
    CreateMaintenanceTypeResponse handleCreate(CreateMaintenanceTypeRequest request);
    GetMaintenanceTypeResponse handleGetAll();
    CreateMaintenanceTypeResponse handleGetById(Long id);
    UpdateMaintenanceTypeResponse handleUpdate(UpdateMaintenanceTypeRequest request);
    void handleDelete(Long id);
}
