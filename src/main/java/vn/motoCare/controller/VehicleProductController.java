package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.vehiclePrd.CreateVehicleProductRequest;
import vn.motoCare.domain.request.vehiclePrd.UpdateVehicleProductRequest;
import vn.motoCare.domain.response.vehiclePrd.CreateVehicleProductResponse;
import vn.motoCare.domain.response.vehiclePrd.UpdateVehicleProductResponse;
import vn.motoCare.service.VehicleProductService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@Slf4j(topic = "VEHICLE-PRODUCT-CONTROLLER")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class VehicleProductController {
    private final VehicleProductService vehicleProductService;

    @PostMapping("/vehicle-products")
    @ApiMessage(message = "Tạo vehicle product thành công")
    public ResponseEntity<CreateVehicleProductResponse> createVehicleProduct(
            @Valid @RequestBody CreateVehicleProductRequest req
    ) throws IdInvalidException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehicleProductService.handleCreate(req));
    }

    @PutMapping("/vehicle-products")
    @ApiMessage(message = "Cập nhật vehicle product thành công")
    public ResponseEntity<UpdateVehicleProductResponse> updateVehicleProduct(
            @Valid @RequestBody UpdateVehicleProductRequest req
    ) throws IdInvalidException {

        return ResponseEntity.ok(
                vehicleProductService.handleUpdate(req)
        );
    }
}
