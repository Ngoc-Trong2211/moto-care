package vn.motoCare.domain.response.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumTypeNotification;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class GetNotificationResponse {
    private List<Notification> notifications;

    @Getter
    @Setter
    public static class Notification {
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
}
