package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.motoCare.domain.UserEntity;
import vn.motoCare.domain.VehicleEntity;

import java.util.List;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long>, JpaSpecificationExecutor<VehicleEntity> {

    /**
     * Distinct users who own at least one vehicle registered at the given agency.
     * Each user is returned once even if they have multiple vehicles at that agency.
     */
    @Query("SELECT DISTINCT v.user FROM VehicleEntity v WHERE v.agency.id = :agencyId AND v.user.email IS NOT NULL AND v.user.email != ''")
    List<UserEntity> findDistinctUsersByAgencyId(@Param("agencyId") Long agencyId);
}
