package vn.motoCare.domain.request.AccessoryPrd;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessoryProductRequest {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long productId;

    @Min(0)
    private long price;

    @Min(0)
    private int quantity;
}
