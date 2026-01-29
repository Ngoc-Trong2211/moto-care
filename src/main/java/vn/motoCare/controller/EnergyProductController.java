package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.energyPrd.CreateEnergyProductRequest;
import vn.motoCare.domain.request.energyPrd.UpdateEnergyProductRequest;
import vn.motoCare.domain.request.energyPrd.EnergyProductSpecificationRequest;
import vn.motoCare.domain.response.energyPrd.CreateEnergyProductResponse;
import vn.motoCare.domain.response.energyPrd.UpdateEnergyProductResponse;
import vn.motoCare.domain.response.energyPrd.GetEnergyProductResponse;
import vn.motoCare.service.EnergyProductService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "ENERGY-PRODUCT-CONTROLLER")
@RequestMapping("/api/v1")
public class EnergyProductController {
    private final EnergyProductService energyProductService;

    /* ===================== CREATE ===================== */

    @PostMapping("/energy-products")
    @ApiMessage(message = "Tạo energy product thành công")
    public ResponseEntity<CreateEnergyProductResponse> create(
            @Valid @RequestBody CreateEnergyProductRequest req
    ) throws IdInvalidException {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(energyProductService.handleCreate(req));
    }

    /* ===================== UPDATE ===================== */

    @PutMapping("/energy-products")
    @ApiMessage(message = "Cập nhật energy product thành công")
    public ResponseEntity<UpdateEnergyProductResponse> update(
            @Valid @RequestBody UpdateEnergyProductRequest req
    ) throws IdInvalidException {

        return ResponseEntity.ok(
                energyProductService.handleUpdate(req)
        );
    }

    /* ===================== GET LIST ===================== */

    @GetMapping("/energy-products")
    @ApiMessage(message = "Lấy danh sách energy product thành công")
    public ResponseEntity<GetEnergyProductResponse> getAll(
            Pageable pageable,
            EnergyProductSpecificationRequest req
    ) {
        return ResponseEntity.ok(
                energyProductService.handleGetEnergyProduct(pageable, req)
        );
    }

    /* ===================== DELETE (SOFT) ===================== */

    @DeleteMapping("/energy-products/{id}")
    @ApiMessage(message = "Xóa energy product thành công")
    public ResponseEntity<String> delete(@PathVariable Long id)
            throws IdInvalidException {

        energyProductService.handleDeleteEnergyProduct(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Cập nhật thành công");
    }
}