package vn.motoCare.domain.request.maintenanceReminder;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumStatusReminder;

import java.time.LocalDate;

@Getter
@Setter
public class CreateMaintenanceReminderRequest {
    @NotNull
    private Long vehicleId;
    @NotNull
    private Long maintenanceTypeId;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private EnumStatusReminder status;
}
