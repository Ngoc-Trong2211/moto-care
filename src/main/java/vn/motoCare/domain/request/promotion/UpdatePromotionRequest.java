package vn.motoCare.domain.request.promotion;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdatePromotionRequest {
    @NotNull
    private Long id;
    @NotNull
    private String title;
    private String description;
    private boolean active;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private Long agencyId;
}
