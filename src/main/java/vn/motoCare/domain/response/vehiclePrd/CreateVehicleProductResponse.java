package vn.motoCare.domain.response.vehiclePrd;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumColor;
import vn.motoCare.util.enumEntity.EnumProductType;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class CreateVehicleProductResponse {
    private Long id;
    private String brand;
    private String model;
    private String name;
    private List<EnumColor> colors;
    private EnumStatusProduct status;
    private EnumProductType type;
    private long price;
    private int quantity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    private String createdBy;
}
