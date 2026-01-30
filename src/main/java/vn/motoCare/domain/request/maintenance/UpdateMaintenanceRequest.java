package vn.motoCare.domain.request.maintenance;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateMaintenanceRequest {
    @NotNull
    private Long id;
    @NotNull
    private Long vehicleId;
    @NotNull
    private Long maintenanceTypeId;
    @NotNull
    private Long agencyId;
    @NotNull
    private LocalDate maintenanceDate;
    @NotNull
    private int km;
    private String note;
    @NotNull
    private LocalDate dueDate;
}
