package vn.motoCare.domain.request.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumAppointmentStatus;

@Getter
@Setter
public class UpdateAppointmentStatusRequest {
    @NotNull
    private EnumAppointmentStatus status;
}
