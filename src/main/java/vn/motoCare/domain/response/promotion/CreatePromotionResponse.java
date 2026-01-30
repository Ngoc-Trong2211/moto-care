package vn.motoCare.domain.response.promotion;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
public class CreatePromotionResponse {
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
