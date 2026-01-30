package vn.motoCare.domain.request.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.motoCare.util.enumEntity.EnumTypeNotification;

@Getter
@Setter
public class CreateNotificationRequest {
    @NotNull
    private Long userId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private EnumTypeNotification type;
}
