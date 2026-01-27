package vn.motoCare.domain.response.user;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserUpdateResponse {
    private Long id;
    private String name;
    private String email;
    private String status;
    private String role;

    private Instant updatedAt;
    private String updatedBy;
}