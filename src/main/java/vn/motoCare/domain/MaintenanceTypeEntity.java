package vn.motoCare.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.service.serviceImpl.AuthServiceImpl;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tbl_maintenance_type")
public class MaintenanceTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int periodKm;
    private int periodMonth;
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "maintenanceType")
    @JsonIgnore
    private List<MaintenanceEntity> maintenances;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "maintenanceType")
    @JsonIgnore
    private List<MaintenanceReminderEntity> maintenanceReminders;

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
