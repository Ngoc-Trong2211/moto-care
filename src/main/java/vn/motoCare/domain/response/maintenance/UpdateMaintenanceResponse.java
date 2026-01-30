package vn.motoCare.domain.response.maintenance;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateMaintenanceResponse {
    private Long id;
    private Long vehicleId;
    private String vehicleLicensePlate;
    private Long maintenanceTypeId;
    private String maintenanceTypeName;
    private Long agencyId;
    private String agencyName;
    private LocalDate maintenanceDate;
    private int km;
    private String note;
    private LocalDate dueDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant updatedAt;
    private String updatedBy;
}
