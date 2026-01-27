package vn.motoCare.service.serviceImpl;

import vn.motoCare.domain.PermissionEntity;
import vn.motoCare.domain.RoleEntity;
import vn.motoCare.domain.request.role.CreateRoleRequest;
import vn.motoCare.domain.request.role.RoleSpecificationRequest;
import vn.motoCare.domain.request.role.UpdateRoleRequest;
import vn.motoCare.domain.response.role.CreateRoleResponse;
import vn.motoCare.domain.response.role.GetRoleResponse;
import vn.motoCare.domain.response.role.UpdateRoleResponse;
import vn.motoCare.repository.PermissionRepository;
import vn.motoCare.repository.RoleRepository;
import vn.motoCare.service.RoleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.motoCare.service.specification.RoleSpecification;
import vn.motoCare.util.exception.IdInvalidException;
import vn.motoCare.util.exception.NameExistsException;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ROLE-SERVICE")
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public RoleEntity findByName(String name) {
        return this.roleRepository.findByName(name);
    }

    @Override
    public RoleEntity findById(Long id) {
        return this.roleRepository.findById(id).isPresent() ? this.roleRepository.findById(id).get() : null;
    }

    @Override
    public boolean existsByName(String name) {
        return this.roleRepository.existsByName(name) == 1;
    }

    @Override
    public CreateRoleResponse handleCreateRole(CreateRoleRequest req) throws IdInvalidException {
        if (this.existsByName(req.getName())) throw new NameExistsException("Tên role đã tồn tại!");
        RoleEntity role = new RoleEntity();
        role.setName(req.getName().toUpperCase());
        role.setDescription(req.getDescription());
        role.setActive(true);

        List<Long> idPermissions = req.getPermissionId();
        List<PermissionEntity> permissions = this.permissionRepository.findByIdIn(idPermissions);

        if (permissions.isEmpty()) throw new IdInvalidException("Không có permission tồn tại!");

        role.setPermissions(permissions);

        this.roleRepository.save(role);
        return convertToCreateResponse(role);
    }

    private CreateRoleResponse convertToCreateResponse(RoleEntity role){
        CreateRoleResponse res = new CreateRoleResponse();
        res.setId(role.getId());
        res.setActive(role.isActive());
        res.setDescription(role.getDescription());
        res.setName(role.getName());
        List<CreateRoleResponse.Permission> permissions = role.getPermissions().stream().map(
                permission -> {
                    CreateRoleResponse.Permission resPermission = new CreateRoleResponse.Permission();
                    resPermission.setEntity(permission.getEntity());
                    resPermission.setPath(permission.getPath());
                    resPermission.setMethod(permission.getMethod());
                    return resPermission;
                }
        ).toList();
        res.setPermissions(permissions);
        return res;
    }

    @Override
    public UpdateRoleResponse handleUpdateRole(UpdateRoleRequest req) throws IdInvalidException {
        RoleEntity role = this.findById(req.getId());
        if (role == null) throw new IdInvalidException("Role không tồn tại!");

        // Kiểm tra tên role đã có ở các id khác chưa
        if (this.existsByName(req.getName()) && !Objects.equals(this.findByName(req.getName()).getId(), req.getId())) throw new NameExistsException("Tên role đã tồn tại!");
        role.setName(req.getName().toUpperCase());
        role.setDescription(req.getDescription());

        Set<Long> currentIdPermission = role.getPermissions().stream().map(
                PermissionEntity::getId
        ).collect(Collectors.toSet());

        Set<Long> idPermissions = new HashSet<>(req.getPermissionId());

        if (!currentIdPermission.equals(idPermissions)){
            List<PermissionEntity> permissions = this.permissionRepository.findByIdIn(req.getPermissionId());

            if (permissions.isEmpty()) throw new IdInvalidException("Không có permission tồn tại!");

            role.setPermissions(permissions);
        }

        this.roleRepository.save(role);

        return convertToUpdateResponse(role);
    }

    private UpdateRoleResponse convertToUpdateResponse(RoleEntity role) {
        UpdateRoleResponse res = new UpdateRoleResponse();
        res.setId(role.getId());
        res.setName(role.getName());
        res.setDescription(role.getDescription());
        res.setActive(role.isActive());
        List<UpdateRoleResponse.Permission> permissions = role.getPermissions().stream().map(
                permission -> {
                    UpdateRoleResponse.Permission resPermission = new UpdateRoleResponse.Permission();
                    resPermission.setEntity(permission.getEntity());
                    resPermission.setPath(permission.getPath());
                    resPermission.setMethod(permission.getMethod());
                    return resPermission;
                }
        ).toList();
        res.setPermissions(permissions);
        return res;
    }

    @Override
    public void handleUpdateActiveRole(Long id, boolean active) throws IdInvalidException {
        RoleEntity role = this.findById(id);
        if (role == null) throw new IdInvalidException("Role không tồn tại!");
        role.setActive(active);
        this.roleRepository.save(role);
    }

    @Override
    public GetRoleResponse handleGetRole(Pageable pageable, RoleSpecificationRequest req) {
        Specification<RoleEntity> spec = RoleSpecification.specRole(req);
        Page<RoleEntity> rolePage = this.roleRepository.findAll(spec, pageable);
        GetRoleResponse res = new GetRoleResponse();
        GetRoleResponse.DataPage resPage = new GetRoleResponse.DataPage();

        resPage.setTotalPages(rolePage.getTotalPages());
        resPage.setSize(rolePage.getSize());
        resPage.setNumber(rolePage.getNumber() + 1);
        resPage.setNumberOfElements(rolePage.getNumberOfElements());
        res.setDataPage(resPage);

        List<GetRoleResponse.Role> roles = rolePage.getContent().stream()
                .map(role -> {
                    GetRoleResponse.Role resRole = new GetRoleResponse.Role();
                    resRole.setId(role.getId());
                    resRole.setActive(role.isActive());
                    resRole.setName(role.getName());

                    List<GetRoleResponse.Role.Permission> permissions = role.getPermissions().stream().map(
                            permission -> {
                                GetRoleResponse.Role.Permission resPermission = new GetRoleResponse.Role.Permission();
                                resPermission.setEntity(permission.getEntity());
                                resPermission.setMethod(permission.getMethod());
                                resPermission.setPath(permission.getPath());
                                return resPermission;
                            }
                    ).toList();
                    resRole.setPermissions(permissions);

                    return resRole;
                }).toList();
        res.setRoles(roles);
        return res;
    }
}
