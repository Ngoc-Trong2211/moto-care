package vn.motoCare.service;

import vn.motoCare.domain.request.maintenanceReminder.CreateMaintenanceReminderRequest;
import vn.motoCare.domain.request.maintenanceReminder.UpdateMaintenanceReminderStatusRequest;
import vn.motoCare.domain.response.maintenanceReminder.CreateMaintenanceReminderResponse;
import vn.motoCare.domain.response.maintenanceReminder.GetMaintenanceReminderResponse;
import vn.motoCare.domain.response.maintenanceReminder.UpdateMaintenanceReminderStatusResponse;

public interface MaintenanceReminderService {
    CreateMaintenanceReminderResponse handleCreate(CreateMaintenanceReminderRequest request);
    CreateMaintenanceReminderResponse handleGetById(Long id);
    GetMaintenanceReminderResponse handleGetByVehicleId(Long vehicleId);
    UpdateMaintenanceReminderStatusResponse handleUpdateStatus(Long id, UpdateMaintenanceReminderStatusRequest request);
    void handleDelete(Long id);
    void checkAndNotifyDueMaintenanceReminders();
}
