package vn.motoCare.domain.response.energyPrd;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumProductType;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

import java.time.Instant;

@Getter
@Setter
public class UpdateEnergyProductResponse {
    private Long id;
    private String name;
    private String description;
    private long price;
    private int quantity;
    private double capacity;
    private EnumStatusProduct status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;
    private String updatedBy;

    private EnumProductType type;
}