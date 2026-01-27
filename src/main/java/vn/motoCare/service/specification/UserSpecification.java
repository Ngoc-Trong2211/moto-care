package vn.motoCare.service.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.motoCare.domain.UserEntity;
import vn.motoCare.domain.request.user.UserSpecificationRequest;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<UserEntity> specUser(UserSpecificationRequest user){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(hasText(user.getName())){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + user.getName().toLowerCase() + "%"));
            }
            if(hasText(user.getEmail())){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + user.getEmail().toLowerCase() + "%"));
            }
            if(hasText(user.getStatus().toString())){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), "%" + user.getStatus().toString().toLowerCase() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static boolean hasText(String string){
        return string != null && !string.trim().isEmpty();
    }
}