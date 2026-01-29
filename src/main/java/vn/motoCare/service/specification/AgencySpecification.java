package vn.motoCare.service.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.motoCare.domain.AgencyEntity;
import vn.motoCare.domain.request.agency.AgencySpecificationRequest;

import java.util.ArrayList;
import java.util.List;

public class AgencySpecification {
    public static boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static Specification<AgencyEntity> specAgency(AgencySpecificationRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (hasText(req.getName())) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + req.getName().toLowerCase() + "%"));
            }
            if (hasText(req.getEmail())) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + req.getEmail().toLowerCase() + "%"));
            }
            if (req.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), req.getActive()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}