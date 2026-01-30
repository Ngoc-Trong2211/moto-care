package vn.motoCare.domain.response.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumAppointmentStatus;

import java.time.Instant;

@Getter
@Setter
public class UpdateAppointmentStatusResponse {
    private Long id;
    private EnumAppointmentStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant updatedAt;
    private String updatedBy;
}
