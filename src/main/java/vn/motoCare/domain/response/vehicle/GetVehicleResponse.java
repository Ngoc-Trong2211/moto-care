package vn.motoCare.domain.response.vehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class GetVehicleResponse {
    private DataPage page;
    private List<Vehicle> vehicles;

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
    public static class Vehicle {
        private Long id;
        private Long userId;
        private String userEmail;
        private String brand;
        private String model;
        private String licensePlate;
        private Long agencyId;
        private String agencyName;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
        private Instant createdAt;
        private String createdBy;
    }
}
