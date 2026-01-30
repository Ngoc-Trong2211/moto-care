package vn.motoCare.domain.response.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumAppointmentStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetAppointmentResponse {
    private DataPage page;
    private List<Appointment> appointments;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataPage {
        private int number;
        private int size;
        private int numberOfElements;
        private int totalPages;
    }

    @Getter
    @Setter
    public static class Appointment {
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
}
