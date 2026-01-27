package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.motoCare.domain.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long>, JpaSpecificationExecutor<RoleEntity> {
    @Query(value = "SELECT EXISTS(SELECT 1 FROM tbl_role WHERE name = :name)", nativeQuery = true)
    Long existsByName(@Param("name") String name);

    @Query(value = "SELECT * FROM tbl_role WHERE name = :name", nativeQuery = true)
    RoleEntity findByName(@Param("name") String name);
}
