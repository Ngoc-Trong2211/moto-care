package vn.motoCare.service.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.motoCare.domain.EnergyProductEntity;
import vn.motoCare.domain.request.energyPrd.EnergyProductSpecificationRequest;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

import java.util.ArrayList;
import java.util.List;

public class EnergyProductSpecification {

    public static Specification<EnergyProductEntity> specEnergyProduct(
            EnergyProductSpecificationRequest req
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (req.getKeyword() != null && !req.getKeyword().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + req.getKeyword().toLowerCase() + "%"));
            }
            if (req.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), req.getStatus()));
            } else {
                predicates.add(cb.notEqual(root.get("status"), EnumStatusProduct.DELETED));
            }
            if (req.getProductId() != null) {
                predicates.add(cb.equal(root.get("product").get("id"), req.getProductId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}