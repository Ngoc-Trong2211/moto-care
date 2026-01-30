package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.maintenance.CreateMaintenanceRequest;
import vn.motoCare.domain.request.maintenance.MaintenanceSpecificationRequest;
import vn.motoCare.domain.request.maintenance.UpdateMaintenanceRequest;
import vn.motoCare.domain.response.maintenance.CreateMaintenanceResponse;
import vn.motoCare.domain.response.maintenance.GetMaintenanceResponse;
import vn.motoCare.domain.response.maintenance.UpdateMaintenanceResponse;
import vn.motoCare.service.MaintenanceService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "MAINTENANCE-CONTROLLER")
@RequestMapping("/api/v1")
public class MaintenanceController {
    private final MaintenanceService maintenanceService;

    @PostMapping("/maintenances")
    @ApiMessage(message = "Tạo bảo dưỡng thành công")
    public ResponseEntity<CreateMaintenanceResponse> createMaintenance(
            @RequestBody @Valid CreateMaintenanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maintenanceService.handleCreate(request));
    }

    @GetMapping("/maintenances")
    @ApiMessage(message = "Lấy danh sách bảo dưỡng thành công")
    public ResponseEntity<GetMaintenanceResponse> getMaintenances(Pageable pageable, MaintenanceSpecificationRequest req) {
        return ResponseEntity.ok(maintenanceService.handleGetMaintenances(pageable, req));
    }

    @GetMapping("/maintenances/{id}")
    @ApiMessage(message = "Lấy bảo dưỡng theo ID thành công")
    public ResponseEntity<CreateMaintenanceResponse> getMaintenanceById(@PathVariable Long id) throws IdInvalidException {
        return ResponseEntity.ok(maintenanceService.handleGetById(id));
    }

    @GetMapping("/maintenances/vehicle/{vehicleId}")
    @ApiMessage(message = "Lấy danh sách bảo dưỡng theo xe thành công")
    public ResponseEntity<GetMaintenanceResponse> getMaintenancesByVehicleId(@PathVariable Long vehicleId) throws IdInvalidException {
        return ResponseEntity.ok(maintenanceService.handleGetByVehicleId(vehicleId));
    }

    @PutMapping("/maintenances")
    @ApiMessage(message = "Cập nhật bảo dưỡng thành công")
    public ResponseEntity<UpdateMaintenanceResponse> updateMaintenance(
            @RequestBody @Valid UpdateMaintenanceRequest request) throws IdInvalidException {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(maintenanceService.handleUpdate(request));
    }

    @DeleteMapping("/maintenances/{id}")
    @ApiMessage(message = "Xóa bảo dưỡng thành công")
    public ResponseEntity<String> deleteMaintenance(@PathVariable Long id) throws IdInvalidException {
        maintenanceService.handleDelete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Xóa thành công");
    }
}
