package vn.motoCare.domain.response.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumStatusOrder;

import java.time.Instant;

@Getter
@Setter
public class UpdateOrderStatusResponse {
    private Long id;
    private EnumStatusOrder status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant updatedAt;
}
