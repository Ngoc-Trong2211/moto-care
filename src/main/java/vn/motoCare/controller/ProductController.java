package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.product.CreateProductRequest;
import vn.motoCare.domain.response.product.GetProductResponse;
import vn.motoCare.domain.response.product.ProductResponse;
import vn.motoCare.service.ProductService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@Slf4j(topic = "PRODUCT -CONTROLLER")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/products")
    @ApiMessage(message = "tạo mới product")
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody @Valid CreateProductRequest request) {
        return ResponseEntity.ok(productService.handleCreate(request));
    }

    @GetMapping("/products")
    @ApiMessage(message = "lấy danh sách product")
    public ResponseEntity<GetProductResponse> getProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.handleGetProduct(pageable));
    }

    @DeleteMapping("/products/{id}")
    @ApiMessage(message = "Xóa product thành công")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) throws IdInvalidException {
        this.productService.handleDeleteProduct(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Cập nhật thành công");
    }
}
