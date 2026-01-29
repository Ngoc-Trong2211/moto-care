package vn.motoCare.domain.request.energyPrd;

import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

@Getter
@Setter
public class EnergyProductSpecificationRequest {
    private String keyword;
    private EnumStatusProduct status;
    private Long productId;
}