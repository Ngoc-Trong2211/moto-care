package vn.motoCare.domain.response.maintenance;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class GetMaintenanceResponse {
    private DataPage page;
    private List<Maintenance> maintenances;

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
    public static class Maintenance {
        private Long id;
        private Long vehicleId;
        private String vehicleLicensePlate;
        private Long maintenanceTypeId;
        private String maintenanceTypeName;
        private Long agencyId;
        private String agencyName;
        private LocalDate maintenanceDate;
        private int km;
        private String note;
        private LocalDate dueDate;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
        private Instant createdAt;
        private String createdBy;
    }
}
