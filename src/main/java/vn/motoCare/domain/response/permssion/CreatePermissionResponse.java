package vn.motoCare.domain.response.permssion;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumMethodPermission;

import java.time.Instant;

@Getter
@Setter
public class CreatePermissionResponse {
    private String path;
    private EnumMethodPermission method;
    private String entity;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    private String createdBy;
}
