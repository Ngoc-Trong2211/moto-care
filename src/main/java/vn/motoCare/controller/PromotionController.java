package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.promotion.CreatePromotionRequest;
import vn.motoCare.domain.request.promotion.UpdatePromotionRequest;
import vn.motoCare.domain.response.promotion.CreatePromotionResponse;
import vn.motoCare.domain.response.promotion.GetPromotionResponse;
import vn.motoCare.domain.response.promotion.UpdatePromotionResponse;
import vn.motoCare.service.PromotionService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "PROMOTION-CONTROLLER")
@RequestMapping("/api/v1")
public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping("/promotions")
    @ApiMessage(message = "Tạo khuyến mãi thành công")
    public ResponseEntity<CreatePromotionResponse> createPromotion(
            @RequestBody @Valid CreatePromotionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionService.handleCreate(request));
    }

    @GetMapping("/promotions")
    @ApiMessage(message = "Lấy danh sách khuyến mãi thành công")
    public ResponseEntity<GetPromotionResponse> getPromotions(Pageable pageable) {
        return ResponseEntity.ok(promotionService.handleGetPromotions(pageable));
    }

    @PutMapping("/promotions")
    @ApiMessage(message = "Cập nhật khuyến mãi thành công")
    public ResponseEntity<UpdatePromotionResponse> updatePromotion(
            @RequestBody @Valid UpdatePromotionRequest request) throws IdInvalidException {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(promotionService.handleUpdate(request));
    }

    @DeleteMapping("/promotions/{id}")
    @ApiMessage(message = "Xóa khuyến mãi thành công")
    public ResponseEntity<String> deletePromotion(@PathVariable Long id) throws IdInvalidException {
        promotionService.handleDelete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Xóa thành công");
    }
}
