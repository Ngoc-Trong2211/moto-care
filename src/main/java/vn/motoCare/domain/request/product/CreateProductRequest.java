package vn.motoCare.domain.request.product;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumProductType;

@Getter
@Setter
public class CreateProductRequest {
    @NotNull
    private EnumProductType type;
    @NotNull
    private Long agencyId;
}