package vn.motoCare.domain.request.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVehicleRequest {
    @NotNull
    private Long userId;
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotBlank
    private String licensePlate;
    @NotNull
    private Long agencyId;
}
