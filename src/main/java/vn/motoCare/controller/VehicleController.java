package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.vehicle.CreateVehicleRequest;
import vn.motoCare.domain.request.vehicle.UpdateVehicleRequest;
import vn.motoCare.domain.response.vehicle.CreateVehicleResponse;
import vn.motoCare.domain.response.vehicle.GetVehicleResponse;
import vn.motoCare.domain.response.vehicle.UpdateVehicleResponse;
import vn.motoCare.service.VehicleService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "VEHICLE-CONTROLLER")
@RequestMapping("/api/v1")
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping("/vehicles")
    @ApiMessage(message = "Tạo xe thành công")
    public ResponseEntity<CreateVehicleResponse> createVehicle(
            @RequestBody @Valid CreateVehicleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.handleCreate(request));
    }

    @GetMapping("/vehicles")
    @ApiMessage(message = "Lấy danh sách xe thành công")
    public ResponseEntity<GetVehicleResponse> getVehicles(Pageable pageable) {
        return ResponseEntity.ok(vehicleService.handleGetVehicles(pageable));
    }

    @PutMapping("/vehicles")
    @ApiMessage(message = "Cập nhật xe thành công")
    public ResponseEntity<UpdateVehicleResponse> updateVehicle(
            @RequestBody @Valid UpdateVehicleRequest request) throws IdInvalidException {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(vehicleService.handleUpdate(request));
    }

    @DeleteMapping("/vehicles/{id}")
    @ApiMessage(message = "Xóa xe thành công")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long id) throws IdInvalidException {
        vehicleService.handleDelete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Xóa thành công");
    }
}
