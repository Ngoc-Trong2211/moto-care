package vn.motoCare.service;

import vn.motoCare.domain.request.notification.CreateNotificationRequest;
import vn.motoCare.domain.response.notification.CreateNotificationResponse;
import vn.motoCare.domain.response.notification.GetNotificationResponse;

public interface NotificationService {
    CreateNotificationResponse handleCreate(CreateNotificationRequest request);
    void createNotificationForUser(Long userId, String title, String content, vn.motoCare.util.enumEntity.EnumTypeNotification type);
    void createNotificationForAllUsers(String title, String content, vn.motoCare.util.enumEntity.EnumTypeNotification type);
    GetNotificationResponse handleGetByUserId(Long userId);
    void handleMarkAsRead(Long id);
    void handleDelete(Long id);
}
