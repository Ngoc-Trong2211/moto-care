package vn.motoCare.domain.request.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailItemRequest {
    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;

    @Min(0)
    private double price;
}
