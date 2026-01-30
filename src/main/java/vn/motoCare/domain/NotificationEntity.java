package vn.motoCare.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumTypeNotification;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "tbl_notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private EnumTypeNotification type;

    private boolean isRead;

    private Instant createdAt;

    @PrePersist
    public void handleCreated() {
        this.createdAt = Instant.now();
        this.isRead = false;
    }
}
