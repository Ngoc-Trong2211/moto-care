package vn.motoCare.domain.request.appointment;

import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumAppointmentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentSpecificationRequest {
    private Long userId;
    private Long vehicleId;
    private Long agencyId;
    private LocalDateTime appointmentDateFrom;
    private LocalDateTime appointmentDateTo;
    private EnumAppointmentStatus status;
}
