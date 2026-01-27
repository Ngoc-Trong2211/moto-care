package vn.motoCare.service.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.motoCare.domain.RoleEntity;
import vn.motoCare.domain.request.role.RoleSpecificationRequest;

import java.util.ArrayList;
import java.util.List;

public class RoleSpecification {
    public static Specification<RoleEntity> specRole(RoleSpecificationRequest req){
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (hasText(req.getName())){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + req.getName().toLowerCase() + "%"));
            }
            predicates.add(criteriaBuilder.equal(root.get("active"), req.isActive()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }));
    }

    public static boolean hasText(String string){
        return string!=null && !string.trim().isEmpty();
    }
}
