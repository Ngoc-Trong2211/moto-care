package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.motoCare.domain.AgencyEntity;

public interface AgencyRepository extends JpaRepository<AgencyEntity, Long> {
}
