package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.order.CreateOrderRequest;
import vn.motoCare.domain.request.order.UpdateOrderStatusRequest;
import vn.motoCare.domain.response.order.CreateOrderResponse;
import vn.motoCare.domain.response.order.UpdateOrderStatusResponse;
import vn.motoCare.service.OrderService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "ORDER-CONTROLLER")
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    @ApiMessage(message = "Tạo đơn hàng thành công")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.handleCreate(request));
    }

    @PatchMapping("/orders/{id}/status")
    @ApiMessage(message = "Cập nhật trạng thái đơn hàng thành công")
    public ResponseEntity<UpdateOrderStatusResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateOrderStatusRequest request) throws IdInvalidException {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(orderService.handleUpdateStatus(id, request));
    }
}
