package vn.motoCare.domain.request.vehiclePrd;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumColor;

import java.util.List;

@Getter
@Setter
public class CreateVehicleProductRequest {
    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotBlank
    private String name;

    @NotNull
    private List<EnumColor> colors;

    @NotNull
    private long price;

    @NotNull
    private int quantity;

    @NotNull
    private Long productId;
}