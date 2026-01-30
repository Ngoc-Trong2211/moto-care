package vn.motoCare.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.service.serviceImpl.AuthServiceImpl;
import vn.motoCare.util.enumEntity.EnumAppointmentStatus;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "tbl_appointment")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    @JsonIgnore
    private VehicleEntity vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = false)
    @JsonIgnore
    private AgencyEntity agency;

    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    private EnumAppointmentStatus status;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleCreated() {
        this.createdAt = Instant.now();
        this.createdBy = AuthServiceImpl.getCurrentUserLogin().isPresent() ? AuthServiceImpl.getCurrentUserLogin().get() : "";
    }

    @PreUpdate
    public void handleUpdated() {
        this.updatedAt = Instant.now();
        this.updatedBy = AuthServiceImpl.getCurrentUserLogin().isPresent() ? AuthServiceImpl.getCurrentUserLogin().get() : "";
    }
}
