package vn.motoCare.domain.request.permission;

import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumMethodPermission;

@Getter
@Setter
public class PermissionSpecificationRequest {
    private EnumMethodPermission method;
    private String entity;
}