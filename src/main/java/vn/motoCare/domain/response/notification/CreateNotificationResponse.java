package vn.motoCare.domain.response.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumTypeNotification;

import java.time.Instant;

@Getter
@Setter
public class CreateNotificationResponse {
    private Long id;
    private Long userId;
    private String userEmail;
    private String title;
    private String content;
    private EnumTypeNotification type;
    private boolean isRead;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Instant createdAt;
}
