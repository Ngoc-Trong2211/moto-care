package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.motoCare.domain.PermissionEntity;
import vn.motoCare.util.enumEntity.EnumMethodPermission;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long>, JpaSpecificationExecutor<PermissionEntity> {
    boolean existsByPathAndMethodAndEntity(String path, EnumMethodPermission method, String entity);
    boolean existsByPathAndMethodAndEntityAndIdNot(String path, EnumMethodPermission method, String entity, Long id);
}
