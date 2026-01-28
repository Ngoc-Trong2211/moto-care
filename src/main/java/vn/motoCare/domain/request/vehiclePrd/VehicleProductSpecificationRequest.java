package vn.motoCare.domain.request.vehiclePrd;

import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

@Getter
@Setter
public class VehicleProductSpecificationRequest {
    private String brand;
    private String model;
    private EnumStatusProduct status;
}
