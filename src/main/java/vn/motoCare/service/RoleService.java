package vn.motoCare.service;

import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.RoleEntity;
import vn.motoCare.domain.request.role.CreateRoleRequest;
import vn.motoCare.domain.request.role.RoleSpecificationRequest;
import vn.motoCare.domain.request.role.UpdateRoleRequest;
import vn.motoCare.domain.response.role.CreateRoleResponse;
import vn.motoCare.domain.response.role.GetRoleResponse;
import vn.motoCare.domain.response.role.UpdateRoleResponse;
import vn.motoCare.util.exception.IdInvalidException;

public interface RoleService {
    RoleEntity findByName(String name);
    RoleEntity findById(Long id);
    boolean existsByName(String name);
    CreateRoleResponse handleCreateRole(CreateRoleRequest req) throws IdInvalidException;
    UpdateRoleResponse handleUpdateRole(UpdateRoleRequest req) throws IdInvalidException;
    void handleUpdateActiveRole(Long id, boolean active) throws IdInvalidException;
    GetRoleResponse handleGetRole(Pageable pageable, RoleSpecificationRequest req);
}
