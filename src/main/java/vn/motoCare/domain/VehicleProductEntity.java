package vn.motoCare.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.service.serviceImpl.AuthServiceImpl;
import vn.motoCare.util.enumEntity.EnumColor;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tbl_prd_vehicle")
public class VehicleProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String model;
    private String name;

    @ElementCollection(targetClass = EnumColor.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "tbl_vehicle_colors",
            joinColumns = @JoinColumn(name = "vehicle_id")
    )
    @Column(name = "color")
    private List<EnumColor> colors;

    @Enumerated(EnumType.STRING)
    private EnumStatusProduct status;

    private long price;
    private int quantity;

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
