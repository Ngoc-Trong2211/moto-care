package vn.motoCare.domain.request.AccessoryPrd;

import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

@Getter
@Setter
public class AccessoryProductSpecificationRequest {

    private String keyword;
    private EnumStatusProduct status;
    private Long productId;
}