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
@Table(name = "tbl_agency")
public class AgencyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String address;
    private int phone;
    private boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @JsonIgnore
    private List<ProductEntity> products;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agencyEntity")
    @JsonIgnore
    private List<PromotionEntity> promotions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @JsonIgnore
    private List<VehicleEntity> vehicles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @JsonIgnore
    private List<AppointmentEntity> appointments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @JsonIgnore
    private List<MaintenanceEntity> maintenances;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleCreated(){
        this.createdAt = Instant.now();
        this.createdBy = AuthServiceImpl.getCurrentUserLogin().isPresent() ? AuthServiceImpl.getCurrentUserLogin().get() : "";
    }

    @PreUpdate
    public void handleUpdated(){
        this.updatedAt = Instant.now();
        this.updatedBy = AuthServiceImpl.getCurrentUserLogin().isPresent() ? AuthServiceImpl.getCurrentUserLogin().get() : "";
    }
}
