package vn.motoCare.domain.response.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumAppointmentStatus;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateAppointmentResponse {
    private Long id;
    private Long userId;
    private String userEmail;
    private Long vehicleId;
    private String vehicleLicensePlate;
    private Long agencyId;
    private String agencyName;
    private LocalDateTime appointmentDate;
    private EnumAppointmentStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant createdAt;
    private String createdBy;
}
