package vn.motoCare.domain.request.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumAppointmentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateAppointmentRequest {
    @NotNull
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private Long vehicleId;
    @NotNull
    private Long agencyId;
    @NotNull
    private LocalDateTime appointmentDate;
    @NotNull
    private EnumAppointmentStatus status;
}
