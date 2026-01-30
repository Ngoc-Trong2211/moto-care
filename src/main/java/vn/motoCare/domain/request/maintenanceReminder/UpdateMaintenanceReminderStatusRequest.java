package vn.motoCare.domain.request.maintenanceReminder;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumStatusReminder;

@Getter
@Setter
public class UpdateMaintenanceReminderStatusRequest {
    @NotNull
    private EnumStatusReminder status;
}
