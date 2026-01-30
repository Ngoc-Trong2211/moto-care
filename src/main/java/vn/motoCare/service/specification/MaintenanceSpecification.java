package vn.motoCare.service.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.motoCare.domain.MaintenanceEntity;
import vn.motoCare.domain.request.maintenance.MaintenanceSpecificationRequest;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceSpecification {

    public static boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static Specification<MaintenanceEntity> specMaintenance(MaintenanceSpecificationRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (req.getVehicleId() != null) {
                predicates.add(cb.equal(root.get("vehicle").get("id"), req.getVehicleId()));
            }
            if (req.getMaintenanceTypeId() != null) {
                predicates.add(cb.equal(root.get("maintenanceType").get("id"), req.getMaintenanceTypeId()));
            }
            if (req.getAgencyId() != null) {
                predicates.add(cb.equal(root.get("agency").get("id"), req.getAgencyId()));
            }
            if (req.getMaintenanceDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("maintenanceDate"), req.getMaintenanceDateFrom()));
            }
            if (req.getMaintenanceDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("maintenanceDate"), req.getMaintenanceDateTo()));
            }
            if (req.getDueDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dueDate"), req.getDueDateFrom()));
            }
            if (req.getDueDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dueDate"), req.getDueDateTo()));
            }
            if (req.getKmMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("km"), req.getKmMin()));
            }
            if (req.getKmMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("km"), req.getKmMax()));
            }
            if (hasText(req.getNote())) {
                predicates.add(cb.like(cb.lower(root.get("note")), "%" + req.getNote().toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
