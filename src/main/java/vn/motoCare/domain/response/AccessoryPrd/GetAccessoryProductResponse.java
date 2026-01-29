package vn.motoCare.domain.response.AccessoryPrd;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumProductType;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class GetAccessoryProductResponse {

    private DataPage page;
    private List<AccessoryProduct> accessories;

    @Getter
    @Setter
    public static class DataPage {
        private int totalPages;
        private int size;
        private int number;
        private int numberOfElements;
    }

    @Getter
    @Setter
    public static class AccessoryProduct {
        private Long id;
        private String name;
        private String description;
        private long price;
        private int quantity;
        private EnumStatusProduct status;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
        private Instant createdAt;
        private String createdBy;

        private EnumProductType type;
    }
}
