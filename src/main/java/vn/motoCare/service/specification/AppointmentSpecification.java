package vn.motoCare.service.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.motoCare.domain.AppointmentEntity;
import vn.motoCare.domain.request.appointment.AppointmentSpecificationRequest;

import java.util.ArrayList;
import java.util.List;

public class AppointmentSpecification {

    public static Specification<AppointmentEntity> specAppointment(AppointmentSpecificationRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (req.getUserId() != null) {
                predicates.add(cb.equal(root.get("user").get("id"), req.getUserId()));
            }
            if (req.getVehicleId() != null) {
                predicates.add(cb.equal(root.get("vehicle").get("id"), req.getVehicleId()));
            }
            if (req.getAgencyId() != null) {
                predicates.add(cb.equal(root.get("agency").get("id"), req.getAgencyId()));
            }
            if (req.getAppointmentDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("appointmentDate"), req.getAppointmentDateFrom()));
            }
            if (req.getAppointmentDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("appointmentDate"), req.getAppointmentDateTo()));
            }
            if (req.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), req.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
