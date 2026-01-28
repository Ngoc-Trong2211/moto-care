package vn.motoCare.domain.request.vehiclePrd;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumColor;
import vn.motoCare.util.enumEntity.EnumProductType;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

import java.util.List;

@Getter
@Setter
public class UpdateVehicleProductRequest {
    @NotNull
    private Long id;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotBlank
    private String name;

    @NotNull
    private List<EnumColor> colors;

    @NotNull
    private EnumStatusProduct status;

    @NotNull
    private EnumProductType type;

    private long price;
    private int quantity;
}
