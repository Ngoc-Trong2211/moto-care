package vn.motoCare.domain.response.agency;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CreateAgencyResponse {
    private Long id;
    private String name;
    private String email;
    private String address;
    private int phone;
    private boolean active;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    private String createdBy;
}