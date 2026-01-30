package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.maintenanceType.CreateMaintenanceTypeRequest;
import vn.motoCare.domain.request.maintenanceType.UpdateMaintenanceTypeRequest;
import vn.motoCare.domain.response.maintenanceType.CreateMaintenanceTypeResponse;
import vn.motoCare.domain.response.maintenanceType.GetMaintenanceTypeResponse;
import vn.motoCare.domain.response.maintenanceType.UpdateMaintenanceTypeResponse;
import vn.motoCare.service.MaintenanceTypeService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "MAINTENANCE-TYPE-CONTROLLER")
@RequestMapping("/api/v1")
public class MaintenanceTypeController {
    private final MaintenanceTypeService maintenanceTypeService;

    @PostMapping("/maintenance-types")
    @ApiMessage(message = "Tạo loại bảo dưỡng thành công")
    public ResponseEntity<CreateMaintenanceTypeResponse> createMaintenanceType(
            @RequestBody @Valid CreateMaintenanceTypeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maintenanceTypeService.handleCreate(request));
    }

    @GetMapping("/maintenance-types")
    @ApiMessage(message = "Lấy danh sách loại bảo dưỡng thành công")
    public ResponseEntity<GetMaintenanceTypeResponse> getAllMaintenanceTypes() {
        return ResponseEntity.ok(maintenanceTypeService.handleGetAll());
    }

    @GetMapping("/maintenance-types/{id}")
    @ApiMessage(message = "Lấy loại bảo dưỡng theo ID thành công")
    public ResponseEntity<CreateMaintenanceTypeResponse> getMaintenanceTypeById(@PathVariable Long id) throws IdInvalidException {
        return ResponseEntity.ok(maintenanceTypeService.handleGetById(id));
    }

    @PutMapping("/maintenance-types")
    @ApiMessage(message = "Cập nhật loại bảo dưỡng thành công")
    public ResponseEntity<UpdateMaintenanceTypeResponse> updateMaintenanceType(
            @RequestBody @Valid UpdateMaintenanceTypeRequest request) throws IdInvalidException {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(maintenanceTypeService.handleUpdate(request));
    }

    @DeleteMapping("/maintenance-types/{id}")
    @ApiMessage(message = "Xóa loại bảo dưỡng thành công")
    public ResponseEntity<String> deleteMaintenanceType(@PathVariable Long id) throws IdInvalidException {
        maintenanceTypeService.handleDelete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Xóa thành công");
    }
}
