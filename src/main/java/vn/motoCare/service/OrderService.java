package vn.motoCare.service;

import vn.motoCare.domain.request.order.CreateOrderRequest;
import vn.motoCare.domain.request.order.UpdateOrderStatusRequest;
import vn.motoCare.domain.response.order.CreateOrderResponse;
import vn.motoCare.domain.response.order.UpdateOrderStatusResponse;

public interface OrderService {
    CreateOrderResponse handleCreate(CreateOrderRequest request);
    UpdateOrderStatusResponse handleUpdateStatus(Long id, UpdateOrderStatusRequest request);
}
