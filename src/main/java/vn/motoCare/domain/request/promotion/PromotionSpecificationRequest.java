package vn.motoCare.domain.request.promotion;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PromotionSpecificationRequest {
    private String title;
    private String description;
    private Boolean active;
    private LocalDate startDateFrom;
    private LocalDate startDateTo;
    private LocalDate endDateFrom;
    private LocalDate endDateTo;
    private Long agencyId;
}
