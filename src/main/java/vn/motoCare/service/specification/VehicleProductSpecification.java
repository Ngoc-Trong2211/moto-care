package vn.motoCare.service.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.motoCare.domain.VehicleProductEntity;
import vn.motoCare.domain.request.vehiclePrd.VehicleProductSpecificationRequest;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

import java.util.ArrayList;
import java.util.List;

public class VehicleProductSpecification {

    public static Specification<VehicleProductEntity> specVehicleProduct(
            VehicleProductSpecificationRequest req
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (req.getBrand() != null && !req.getBrand().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("brand")),
                        "%" + req.getBrand().toLowerCase() + "%"
                ));
            }
            if (req.getModel() != null && !req.getModel().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("model")),
                        "%" + req.getModel().toLowerCase() + "%"
                ));
            }
            if (req.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), req.getStatus()));
            }
            predicates.add(
                    cb.notEqual(root.get("status"), EnumStatusProduct.DELETED)
            );
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}