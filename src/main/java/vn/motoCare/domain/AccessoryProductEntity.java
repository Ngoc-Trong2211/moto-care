package vn.motoCare.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.service.serviceImpl.AuthServiceImpl;
import vn.motoCare.util.enumEntity.EnumStatusProduct;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "tbl_accessory_product")
public class AccessoryProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private EnumStatusProduct status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Min(0)
    private long price;

    @Min(0)
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
