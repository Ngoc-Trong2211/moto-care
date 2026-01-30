package vn.motoCare.domain.response.promotion;

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
public class GetPromotionResponse {
    private DataPage page;
    private List<Promotion> promotions;

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
    public static class Promotion {
        private Long id;
        private String title;
        private String description;
        private boolean active;
        private LocalDate startDate;
        private LocalDate endDate;
        private Long agencyId;
        private String agencyName;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
        private Instant createdAt;
        private String createdBy;
    }
}
