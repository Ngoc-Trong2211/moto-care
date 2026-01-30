package vn.motoCare.service.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Context for rendering the promotion email HTML template.
 * All fields are optional for the template; discountValue may be null.
 */
@Getter
@Builder
public class PromotionEmailContext {
    private final String promotionTitle;
    private final String promotionDescription;
    private final String discountValue;
    private final String startDate;
    private final String endDate;
    private final String agencyName;
}
