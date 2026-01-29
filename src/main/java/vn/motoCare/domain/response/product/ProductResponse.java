package vn.motoCare.domain.response.product;

import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumProductType;

import java.time.Instant;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private EnumProductType type;

    private Long agencyId;
    private String agencyName;

    private String description;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}