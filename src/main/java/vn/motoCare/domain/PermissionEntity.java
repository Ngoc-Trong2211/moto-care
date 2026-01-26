package vn.motoCare.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumMethodPermission;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "tbl_permission")
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;
    private String entity;

    @Enumerated(EnumType.STRING)
    private EnumMethodPermission method;

    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleCreated(){
        this.createdAt = Instant.now();
        this.createdBy = null;
    }

    @PreUpdate
    public void handleUpdated(){
        this.updatedAt = Instant.now();
        this.updatedBy = null;
    }
}
