package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.motoCare.domain.PromotionEntity;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Long>, JpaSpecificationExecutor<PromotionEntity> {
}
