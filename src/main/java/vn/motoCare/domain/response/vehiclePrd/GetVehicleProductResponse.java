package vn.motoCare.domain.response.vehiclePrd;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumColor;
import vn.motoCare.util.enumEntity.EnumProductType;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class GetVehicleProductResponse {
    private DataPage page;
    private List<VehicleProduct> products;

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
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VehicleProduct {
        private Long id;
        private String brand;
        private String model;
        private String name;
        private List<EnumColor> colors;
        private EnumStatusProduct status;
        private EnumProductType type;
        private long price;
        private int quantity;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
        private Instant createdAt;
        private String createdBy;
    }
}
