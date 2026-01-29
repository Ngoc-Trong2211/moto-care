package vn.motoCare.service.specification;

import org.springframework.data.jpa.domain.Specification;
import vn.motoCare.domain.AccessoryProductEntity;
import vn.motoCare.domain.request.AccessoryPrd.AccessoryProductSpecificationRequest;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class AccessoryProductSpecification {

    public static Specification<AccessoryProductEntity> specAccessoryProduct(AccessoryProductSpecificationRequest req) {
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