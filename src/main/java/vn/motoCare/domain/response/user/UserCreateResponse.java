package vn.motoCare.domain.response.user;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserCreateResponse {
    private Long id;
    private String name;
    private String email;
    private String role;

    private Instant createdAt;
    private String createdBy;
}
