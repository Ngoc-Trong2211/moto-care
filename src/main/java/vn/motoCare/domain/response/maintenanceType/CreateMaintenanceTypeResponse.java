package vn.motoCare.domain.response.maintenanceType;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CreateMaintenanceTypeResponse {
    private Long id;
    private String name;
    private int periodKm;
    private int periodMonth;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant createdAt;
    private String createdBy;
}
