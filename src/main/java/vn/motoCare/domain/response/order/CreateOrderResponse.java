package vn.motoCare.domain.response.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumStatusOrder;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class CreateOrderResponse {
    private Long id;
    private Long userId;
    private Long agencyId;
    private EnumStatusOrder status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant updatedAt;

    private List<OrderDetailItemResponse> details;

    @Getter
    @Setter
    public static class OrderDetailItemResponse {
        private Long id;
        private Long productId;
        private int quantity;
        private double price;
    }
}
