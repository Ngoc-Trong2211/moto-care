package vn.motoCare.domain.response.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import vn.motoCare.util.enumEntity.EnumProductType;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class GetProductResponse {
    private DataPage page;
    private List<Product> products;

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
    public static class Product {
        private Long id;
        private EnumProductType type;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
        private Instant createdAt;
        private String createdBy;
    }
}
