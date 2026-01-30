package vn.motoCare.domain.request.vehicle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleSpecificationRequest {
    private String brand;
    private String model;
    private String licensePlate;
    private Long userId;
    private Long agencyId;
}
