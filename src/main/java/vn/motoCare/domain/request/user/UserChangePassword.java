package vn.motoCare.domain.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePassword {
    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "CurrentPassword không được để trống")
    private String currentPassword;

    @NotBlank(message = "Password không được để trống")
    private String newPassword;

    @NotBlank(message = "ConfirmPassword không được để trống")
    private String confirmPassword;
}
