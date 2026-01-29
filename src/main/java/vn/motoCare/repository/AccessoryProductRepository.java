package vn.motoCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.motoCare.domain.AccessoryProductEntity;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

@Repository
public interface AccessoryProductRepository
        extends JpaRepository<AccessoryProductEntity, Long>,
        JpaSpecificationExecutor<AccessoryProductEntity> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByIdAndStatus(Long id, EnumStatusProduct status);
}