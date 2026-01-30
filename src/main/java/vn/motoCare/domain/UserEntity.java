package vn.motoCare.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.StatusEnumUser;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "tbl_user")
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private int failedTry;

    @Enumerated(EnumType.STRING)
    private StatusEnumUser status;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<VehicleEntity> vehicles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<AppointmentEntity> appointments;

    @PrePersist
    public void handleCreated(){
        this.createdAt = Instant.now();
        this.createdBy = this.email;
    }

    @PreUpdate
    public void handleUpdated(){
        this.updatedAt = Instant.now();
        this.updatedBy = this.email;
    }
}
