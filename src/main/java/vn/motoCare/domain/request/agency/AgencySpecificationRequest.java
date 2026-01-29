package vn.motoCare.domain.request.agency;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgencySpecificationRequest {
    private String name;
    private String email;
    private Boolean active;
}