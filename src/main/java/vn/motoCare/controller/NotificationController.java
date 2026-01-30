package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.notification.CreateNotificationRequest;
import vn.motoCare.domain.response.notification.CreateNotificationResponse;
import vn.motoCare.domain.response.notification.GetNotificationResponse;
import vn.motoCare.service.NotificationService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "NOTIFICATION-CONTROLLER")
@RequestMapping("/api/v1")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/notifications")
    @ApiMessage(message = "Tạo thông báo thành công")
    public ResponseEntity<CreateNotificationResponse> createNotification(
            @RequestBody @Valid CreateNotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.handleCreate(request));
    }

    @GetMapping("/notifications/user/{userId}")
    @ApiMessage(message = "Lấy danh sách thông báo theo user thành công")
    public ResponseEntity<GetNotificationResponse> getNotificationsByUserId(@PathVariable Long userId) throws IdInvalidException {
        return ResponseEntity.ok(notificationService.handleGetByUserId(userId));
    }

    @PatchMapping("/notifications/{id}/read")
    @ApiMessage(message = "Đánh dấu thông báo đã đọc thành công")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) throws IdInvalidException {
        notificationService.handleMarkAsRead(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Đánh dấu đã đọc thành công");
    }

    @DeleteMapping("/notifications/{id}")
    @ApiMessage(message = "Xóa thông báo thành công")
    public ResponseEntity<String> deleteNotification(@PathVariable Long id) throws IdInvalidException {
        notificationService.handleDelete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Xóa thành công");
    }
}
