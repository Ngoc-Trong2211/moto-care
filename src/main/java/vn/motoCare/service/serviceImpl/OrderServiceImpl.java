package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.motoCare.domain.OrderDetailEntity;
import vn.motoCare.domain.OrderEntity;
import vn.motoCare.domain.request.order.CreateOrderRequest;
import vn.motoCare.domain.request.order.OrderDetailItemRequest;
import vn.motoCare.domain.request.order.UpdateOrderStatusRequest;
import vn.motoCare.domain.response.order.CreateOrderResponse;
import vn.motoCare.domain.response.order.UpdateOrderStatusResponse;
import vn.motoCare.repository.OrderRepository;
import vn.motoCare.repository.UserRepository;
import vn.motoCare.repository.AgencyRepository;
import vn.motoCare.service.NotificationService;
import vn.motoCare.service.OrderService;
import vn.motoCare.util.enumEntity.EnumStatusOrder;
import vn.motoCare.util.enumEntity.EnumTypeNotification;
import vn.motoCare.util.exception.IdInvalidException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "ORDER-SERVICE")
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public CreateOrderResponse handleCreate(CreateOrderRequest request) {
        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại user này!"));
        agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại agency này!"));

        OrderEntity order = new OrderEntity();
        order.setUserId(request.getUserId());
        order.setAgencyId(request.getAgencyId());
        order.setStatus(request.getStatus());

        List<OrderDetailEntity> details = new ArrayList<>();
        for (OrderDetailItemRequest item : request.getDetails()) {
            OrderDetailEntity detail = new OrderDetailEntity();
            detail.setOrder(order);
            detail.setProductId(item.getProductId());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getPrice());
            details.add(detail);
        }
        order.setDetails(details);

        OrderEntity saved = orderRepository.save(order);
        return toCreateResponse(saved);
    }

    @Override
    @Transactional
    public UpdateOrderStatusResponse handleUpdateStatus(Long id, UpdateOrderStatusRequest request) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại đơn hàng này!"));

        EnumStatusOrder oldStatus = order.getStatus();
        if (oldStatus == request.getStatus()) {
            return toUpdateStatusResponse(order);
        }

        order.setStatus(request.getStatus());
        OrderEntity saved = orderRepository.save(order);

        createOrderStatusNotification(saved.getUserId(), request.getStatus());

        return toUpdateStatusResponse(saved);
    }

    private void createOrderStatusNotification(Long userId, EnumStatusOrder status) {
        String title;
        String content;
        switch (status) {
            case CONFIRM -> {
                title = "Order confirmed";
                content = "Your order has been confirmed.";
            }
            case COMPLETE -> {
                title = "Order completed";
                content = "Your order has been completed.";
            }
            case CANCEL -> {
                title = "Order canceled";
                content = "Your order has been canceled.";
            }
            default -> {
                title = "Order status update";
                content = "Your order status has been updated to " + status.name();
            }
        }
        notificationService.createNotificationForUser(userId, title, content, EnumTypeNotification.ORDER);
    }

    private static CreateOrderResponse toCreateResponse(OrderEntity entity) {
        CreateOrderResponse res = new CreateOrderResponse();
        res.setId(entity.getId());
        res.setUserId(entity.getUserId());
        res.setAgencyId(entity.getAgencyId());
        res.setStatus(entity.getStatus());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        List<CreateOrderResponse.OrderDetailItemResponse> detailResponses = entity.getDetails().stream()
                .map(OrderServiceImpl::toDetailItemResponse)
                .toList();
        res.setDetails(detailResponses);
        return res;
    }

    private static CreateOrderResponse.OrderDetailItemResponse toDetailItemResponse(OrderDetailEntity d) {
        CreateOrderResponse.OrderDetailItemResponse item = new CreateOrderResponse.OrderDetailItemResponse();
        item.setId(d.getId());
        item.setProductId(d.getProductId());
        item.setQuantity(d.getQuantity());
        item.setPrice(d.getPrice());
        return item;
    }

    private static UpdateOrderStatusResponse toUpdateStatusResponse(OrderEntity entity) {
        UpdateOrderStatusResponse res = new UpdateOrderStatusResponse();
        res.setId(entity.getId());
        res.setStatus(entity.getStatus());
        res.setUpdatedAt(entity.getUpdatedAt());
        return res;
    }
}
