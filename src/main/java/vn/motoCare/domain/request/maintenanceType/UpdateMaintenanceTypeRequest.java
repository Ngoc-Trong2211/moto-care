package vn.motoCare.domain.request.maintenanceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMaintenanceTypeRequest {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private int periodKm;
    @NotNull
    private int periodMonth;
    private String description;
}
