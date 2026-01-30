package vn.motoCare.domain.response.maintenanceReminder;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumStatusReminder;

import java.time.Instant;

@Getter
@Setter
public class UpdateMaintenanceReminderStatusResponse {
    private Long id;
    private EnumStatusReminder status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant updatedAt;
    private String updatedBy;
}
