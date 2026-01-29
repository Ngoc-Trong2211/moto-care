package vn.motoCare.domain.response.agency;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UpdateAgencyResponse {
    private String name;
    private String email;
    private String address;
    private int phone;
    private boolean active;

    private Instant updatedAt;
    private String updatedBy;
}