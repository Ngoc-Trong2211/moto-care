package vn.motoCare.domain.response.maintenanceReminder;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumStatusReminder;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
public class CreateMaintenanceReminderResponse {
    private Long id;
    private Long vehicleId;
    private String vehicleLicensePlate;
    private Long maintenanceTypeId;
    private String maintenanceTypeName;
    private LocalDate dueDate;
    private EnumStatusReminder status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant createdAt;
    private String createdBy;
}
