package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.motoCare.domain.PermissionEntity;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
}
