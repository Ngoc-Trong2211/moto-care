package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.maintenanceReminder.CreateMaintenanceReminderRequest;
import vn.motoCare.domain.request.maintenanceReminder.UpdateMaintenanceReminderStatusRequest;
import vn.motoCare.domain.response.maintenanceReminder.CreateMaintenanceReminderResponse;
import vn.motoCare.domain.response.maintenanceReminder.GetMaintenanceReminderResponse;
import vn.motoCare.domain.response.maintenanceReminder.UpdateMaintenanceReminderStatusResponse;
import vn.motoCare.service.MaintenanceReminderService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "MAINTENANCE-REMINDER-CONTROLLER")
@RequestMapping("/api/v1")
public class MaintenanceReminderController {
    private final MaintenanceReminderService maintenanceReminderService;

    @PostMapping("/maintenance-reminders")
    @ApiMessage(message = "Tạo nhắc nhở bảo dưỡng thành công")
    public ResponseEntity<CreateMaintenanceReminderResponse> createMaintenanceReminder(
            @RequestBody @Valid CreateMaintenanceReminderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maintenanceReminderService.handleCreate(request));
    }

    @GetMapping("/maintenance-reminders/{id}")
    @ApiMessage(message = "Lấy nhắc nhở bảo dưỡng theo ID thành công")
    public ResponseEntity<CreateMaintenanceReminderResponse> getMaintenanceReminderById(@PathVariable Long id) throws IdInvalidException {
        return ResponseEntity.ok(maintenanceReminderService.handleGetById(id));
    }

    @GetMapping("/maintenance-reminders/vehicle/{vehicleId}")
    @ApiMessage(message = "Lấy danh sách nhắc nhở bảo dưỡng theo xe thành công")
    public ResponseEntity<GetMaintenanceReminderResponse> getMaintenanceRemindersByVehicleId(@PathVariable Long vehicleId) throws IdInvalidException {
        return ResponseEntity.ok(maintenanceReminderService.handleGetByVehicleId(vehicleId));
    }

    @PatchMapping("/maintenance-reminders/{id}/status")
    @ApiMessage(message = "Cập nhật trạng thái nhắc nhở bảo dưỡng thành công")
    public ResponseEntity<UpdateMaintenanceReminderStatusResponse> updateMaintenanceReminderStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMaintenanceReminderStatusRequest request) throws IdInvalidException {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(maintenanceReminderService.handleUpdateStatus(id, request));
    }

    @DeleteMapping("/maintenance-reminders/{id}")
    @ApiMessage(message = "Xóa nhắc nhở bảo dưỡng thành công")
    public ResponseEntity<String> deleteMaintenanceReminder(@PathVariable Long id) throws IdInvalidException {
        maintenanceReminderService.handleDelete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Xóa thành công");
    }
}
