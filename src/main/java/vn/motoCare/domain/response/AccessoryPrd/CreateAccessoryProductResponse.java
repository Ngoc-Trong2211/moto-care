package vn.motoCare.domain.response.AccessoryPrd;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumStatusProduct;
import vn.motoCare.util.enumEntity.EnumProductType;

import java.time.Instant;

@Getter
@Setter
public class CreateAccessoryProductResponse {

    private Long id;
    private String name;
    private String description;
    private long price;
    private int quantity;
    private EnumStatusProduct status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    private String createdBy;

    private EnumProductType type;
}