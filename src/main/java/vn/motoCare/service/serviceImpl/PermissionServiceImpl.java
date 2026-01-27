package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.motoCare.domain.PermissionEntity;
import vn.motoCare.domain.request.permission.CreatePermissionRequest;
import vn.motoCare.domain.request.permission.PermissionSpecificationRequest;
import vn.motoCare.domain.request.permission.UpdatePermissionRequest;
import vn.motoCare.domain.response.permssion.CreatePermissionResponse;
import vn.motoCare.domain.response.permssion.GetPermissionResponse;
import vn.motoCare.domain.response.permssion.UpdatePermissionResponse;
import vn.motoCare.repository.PermissionRepository;
import vn.motoCare.service.PermissionService;
import vn.motoCare.service.specification.PermissionSpecification;
import vn.motoCare.util.exception.IdInvalidException;

import java.util.List;

@Service
@Slf4j(topic = "PERMISSION-SERVICE")
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public CreatePermissionResponse handleCreatePermission(CreatePermissionRequest req)
            throws IdInvalidException {

        if (permissionRepository.existsByPathAndMethodAndEntity(req.getPath(), req.getMethod(), req.getEntity())) {
            throw new IdInvalidException("Permission đã tồn tại!");
        }

        PermissionEntity permission = new PermissionEntity();
        permission.setPath(req.getPath());
        permission.setEntity(req.getEntity());
        permission.setMethod(req.getMethod());
        permission.setDescription(req.getDescription());

        permissionRepository.save(permission);

        return convertToCreateResponse(permission);
    }

    private CreatePermissionResponse convertToCreateResponse(PermissionEntity permission) {
        CreatePermissionResponse res = new CreatePermissionResponse();
        res.setPath(permission.getPath());
        res.setEntity(permission.getEntity());
        res.setMethod(permission.getMethod());
        res.setDescription(permission.getDescription());
        res.setCreatedAt(permission.getCreatedAt());
        res.setCreatedBy(permission.getCreatedBy());
        return res;
    }

    @Override
    public UpdatePermissionResponse handleUpdatePermission(UpdatePermissionRequest req
    ) throws IdInvalidException {

        PermissionEntity permission = permissionRepository.findById(req.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy permission!"));

        if (permissionRepository.existsByPathAndMethodAndEntityAndIdNot(
                req.getPath(),
                req.getMethod(),
                req.getEntity(),
                req.getId()
        )) {
            throw new IdInvalidException("Permission đã tồn tại!");
        }

        permission.setPath(req.getPath());
        permission.setEntity(req.getEntity());
        permission.setMethod(req.getMethod());
        permission.setDescription(req.getDescription());

        permissionRepository.save(permission);

        return convertToUpdateResponse(permission);
    }

    private UpdatePermissionResponse convertToUpdateResponse(
            PermissionEntity permission
    ) {
        UpdatePermissionResponse res = new UpdatePermissionResponse();
        res.setPath(permission.getPath());
        res.setEntity(permission.getEntity());
        res.setMethod(permission.getMethod());
        res.setDescription(permission.getDescription());
        res.setUpdatedAt(permission.getUpdatedAt());
        res.setUpdatedBy(permission.getUpdatedBy());
        return res;
    }

    @Override
    public void handleDeletePermission(Long id) throws IdInvalidException {
        PermissionEntity permission = this.permissionRepository.findById(id).orElseThrow(() -> new IdInvalidException("Quyền hạn này không tồn tại!"));
        permission.getRoles().clear();
        this.permissionRepository.deleteRelationshipPermissionId(permission.getId());
        this.permissionRepository.deleteById(id);
    }

    @Override
    public GetPermissionResponse handleGetPermission(Pageable pageable, PermissionSpecificationRequest req) {
        Specification<PermissionEntity> spec = PermissionSpecification.specPermission(req);
        Page<PermissionEntity> pagePermission = this.permissionRepository.findAll(spec, pageable);

        GetPermissionResponse res = new GetPermissionResponse();
        GetPermissionResponse.DataPage resPage = new GetPermissionResponse.DataPage();

        resPage.setTotalPages(resPage.getTotalPages());
        resPage.setSize(resPage.getSize());
        resPage.setNumber(resPage.getNumber() + 1);
        resPage.setNumberOfElements(resPage.getNumberOfElements());
        res.setPage(resPage);

        List<GetPermissionResponse.Permission> permissions = pagePermission.getContent().stream()
                .map(permission -> {
                    GetPermissionResponse.Permission resPermission = new GetPermissionResponse.Permission();
                    resPermission.setId(permission.getId());
                    resPermission.setPath(permission.getPath());
                    resPermission.setMethod(permission.getMethod());
                    resPermission.setEntity(permission.getEntity());
                    resPermission.setDescription(permission.getDescription());
                    resPermission.setCreatedAt(permission.getCreatedAt());
                    resPermission.setCreatedBy(permission.getCreatedBy());
                    return resPermission;
                }).toList();
        res.setPermissions(permissions);
        return res;
    }
}