package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.motoCare.domain.EnergyProductEntity;

public interface EnergyProductRepository extends JpaRepository<EnergyProductEntity, Long>,
        JpaSpecificationExecutor<EnergyProductEntity> {
}
