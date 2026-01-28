package vn.motoCare.domain.response.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResult {
    private String refreshToken;
    private LoginResponse loginResponse;
}
