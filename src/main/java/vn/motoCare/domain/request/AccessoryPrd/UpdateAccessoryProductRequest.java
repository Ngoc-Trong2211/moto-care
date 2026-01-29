package vn.motoCare.domain.request.AccessoryPrd;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

@Getter
@Setter
public class UpdateAccessoryProductRequest {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long productId;

    @Min(0)
    private long price;

    @Min(0)
    private int quantity;

    @NotNull
    private EnumStatusProduct status;
}
