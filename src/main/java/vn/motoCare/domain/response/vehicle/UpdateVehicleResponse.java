package vn.motoCare.domain.response.vehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UpdateVehicleResponse {
    private Long id;
    private Long userId;
    private String userEmail;
    private String brand;
    private String model;
    private String licensePlate;
    private Long agencyId;
    private String agencyName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant updatedAt;
    private String updatedBy;
}
