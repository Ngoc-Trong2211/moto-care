package vn.motoCare.domain.request.maintenance;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MaintenanceSpecificationRequest {
    private Long vehicleId;
    private Long maintenanceTypeId;
    private Long agencyId;
    private LocalDate maintenanceDateFrom;
    private LocalDate maintenanceDateTo;
    private LocalDate dueDateFrom;
    private LocalDate dueDateTo;
    private Integer kmMin;
    private Integer kmMax;
    private String note;
}
