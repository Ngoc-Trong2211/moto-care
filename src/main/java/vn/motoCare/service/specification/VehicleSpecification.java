package vn.motoCare.service.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.motoCare.domain.VehicleEntity;
import vn.motoCare.domain.request.vehicle.VehicleSpecificationRequest;

import java.util.ArrayList;
import java.util.List;

public class VehicleSpecification {

    public static boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static Specification<VehicleEntity> specVehicle(VehicleSpecificationRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (hasText(req.getBrand())) {
                predicates.add(cb.like(cb.lower(root.get("brand")), "%" + req.getBrand().toLowerCase() + "%"));
            }
            if (hasText(req.getModel())) {
                predicates.add(cb.like(cb.lower(root.get("model")), "%" + req.getModel().toLowerCase() + "%"));
            }
            if (hasText(req.getLicensePlate())) {
                predicates.add(cb.like(cb.lower(root.get("licensePlate")), "%" + req.getLicensePlate().toLowerCase() + "%"));
            }
            if (req.getUserId() != null) {
                predicates.add(cb.equal(root.get("user").get("id"), req.getUserId()));
            }
            if (req.getAgencyId() != null) {
                predicates.add(cb.equal(root.get("agency").get("id"), req.getAgencyId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
