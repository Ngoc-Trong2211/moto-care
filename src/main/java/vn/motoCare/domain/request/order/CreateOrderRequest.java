package vn.motoCare.domain.request.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumStatusOrder;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long agencyId;

    @NotNull
    private EnumStatusOrder status;

    @NotEmpty
    @Valid
    private List<OrderDetailItemRequest> details;
}
