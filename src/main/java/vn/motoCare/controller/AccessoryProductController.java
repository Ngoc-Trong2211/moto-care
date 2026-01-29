package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.service.AccessoryProductService;
import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.request.AccessoryPrd.AccessoryProductSpecificationRequest;
import vn.motoCare.domain.request.AccessoryPrd.CreateAccessoryProductRequest;
import vn.motoCare.domain.request.AccessoryPrd.UpdateAccessoryProductRequest;
import vn.motoCare.domain.response.AccessoryPrd.CreateAccessoryProductResponse;
import vn.motoCare.domain.response.AccessoryPrd.GetAccessoryProductResponse;
import vn.motoCare.domain.response.AccessoryPrd.UpdateAccessoryProductResponse;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@Slf4j(topic = "ACCESSORY-PRODUCT-CONTROLLER")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AccessoryProductController {
    private final AccessoryProductService accessoryProductService;

    @PostMapping("/accessory-products")
    @ApiMessage(message = "Tạo accessory product thành công")
    public ResponseEntity<CreateAccessoryProductResponse> create(
            @Valid @RequestBody CreateAccessoryProductRequest req
    ) throws IdInvalidException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accessoryProductService.handleCreate(req));
    }

    @PutMapping("/accessory-products")
    @ApiMessage(message = "Cập nhật accessory product thành công")
    public ResponseEntity<UpdateAccessoryProductResponse> update(
            @Valid @RequestBody UpdateAccessoryProductRequest req
    ) throws IdInvalidException {
        return ResponseEntity.ok(
                accessoryProductService.handleUpdate(req)
        );
    }

    @GetMapping("/accessory-products")
    @ApiMessage(message = "Lấy danh sách accessory product thành công")
    public ResponseEntity<GetAccessoryProductResponse> getAll(
            Pageable pageable,
            AccessoryProductSpecificationRequest req
    ) {
        return ResponseEntity.ok(
                accessoryProductService.handleGetAccessoryProduct(pageable, req)
        );
    }

    @DeleteMapping("/accessory-products/{id}")
    @ApiMessage(message = "Xóa accessory product thành công")
    public ResponseEntity<String> delete(@PathVariable Long id)
            throws IdInvalidException {
        accessoryProductService.handleDeleteAccessoryProduct(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Cập nhật thành công");
    }
}
