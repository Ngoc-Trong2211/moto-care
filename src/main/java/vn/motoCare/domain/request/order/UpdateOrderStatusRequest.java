package vn.motoCare.domain.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumStatusOrder;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    @NotNull
    private EnumStatusOrder status;
}
