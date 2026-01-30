package vn.motoCare.service.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.motoCare.domain.PromotionEntity;
import vn.motoCare.domain.request.promotion.PromotionSpecificationRequest;

import java.util.ArrayList;
import java.util.List;

public class PromotionSpecification {

    public static boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static Specification<PromotionEntity> specPromotion(PromotionSpecificationRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (hasText(req.getTitle())) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + req.getTitle().toLowerCase() + "%"));
            }
            if (hasText(req.getDescription())) {
                predicates.add(cb.like(cb.lower(root.get("description")), "%" + req.getDescription().toLowerCase() + "%"));
            }
            if (req.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), req.getActive()));
            }
            if (req.getStartDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), req.getStartDateFrom()));
            }
            if (req.getStartDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startDate"), req.getStartDateTo()));
            }
            if (req.getEndDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("endDate"), req.getEndDateFrom()));
            }
            if (req.getEndDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), req.getEndDateTo()));
            }
            if (req.getAgencyId() != null) {
                predicates.add(cb.equal(root.get("agencyEntity").get("id"), req.getAgencyId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
