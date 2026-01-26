package vn.motoCare.service.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.motoCare.domain.PermissionEntity;
import vn.motoCare.domain.request.permission.PermissionSpecificationRequest;

import java.util.ArrayList;
import java.util.List;

public class PermissionSpecification {
    public static boolean hasText(String string){
        return string!=null && !string.trim().isEmpty();
    }

    public static Specification<PermissionEntity> specPermission(PermissionSpecificationRequest req){
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (hasText(req.getEntity())){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("entity")), "%" + req.getEntity().toLowerCase() + "%"));
            }
            if (req.getMethod()!=null){
                predicates.add(criteriaBuilder.equal(root.get("method"), req.getMethod()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }));
    }
}
