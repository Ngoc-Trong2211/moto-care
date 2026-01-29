package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.motoCare.domain.AgencyEntity;

public interface AgencyRepository extends JpaRepository<AgencyEntity, Long>, JpaSpecificationExecutor<AgencyEntity> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}
