package vn.motoCare.service;

import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.request.permission.CreatePermissionRequest;
import vn.motoCare.domain.request.permission.PermissionSpecificationRequest;
import vn.motoCare.domain.request.permission.UpdatePermissionRequest;
import vn.motoCare.domain.response.permssion.CreatePermissionResponse;
import vn.motoCare.domain.response.permssion.GetPermissionResponse;
import vn.motoCare.domain.response.permssion.UpdatePermissionResponse;
import vn.motoCare.util.exception.IdInvalidException;

public interface PermissionService {
    CreatePermissionResponse handleCreatePermission(CreatePermissionRequest req)
            throws IdInvalidException;
    UpdatePermissionResponse handleUpdatePermission(UpdatePermissionRequest req) throws IdInvalidException;
    void handleDeletePermission(Long id) throws IdInvalidException;
    GetPermissionResponse handleGetPermission(Pageable pageable, PermissionSpecificationRequest req);
}
