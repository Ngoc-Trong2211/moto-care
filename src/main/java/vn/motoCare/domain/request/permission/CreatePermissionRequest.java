package vn.motoCare.domain.request.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumMethodPermission;

@Getter
@Setter
public class CreatePermissionRequest {
    @NotBlank(message = "Path không được để trống")
    private String path;

    @NotBlank(message = "Entity không được để trống")
    private String entity;

    @NotNull(message = "Method không được để trống")
    private EnumMethodPermission method;

    private String description;
}
