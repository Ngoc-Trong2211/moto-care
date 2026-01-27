package vn.motoCare.domain.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {
    @NotBlank(message = "Tên người dùng không được để trống!")
    private String name;
    @NotBlank(message = "Email người dùng không được để trống!")
    private String email;
    @NotBlank(message = "Password không được để trống!")
    private String password;
    @NotNull(message = "Role không được để trống!")
    private Long roleId;
}
