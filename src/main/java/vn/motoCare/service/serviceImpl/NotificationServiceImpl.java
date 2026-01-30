package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.motoCare.domain.NotificationEntity;
import vn.motoCare.domain.UserEntity;
import vn.motoCare.domain.request.notification.CreateNotificationRequest;
import vn.motoCare.domain.response.notification.CreateNotificationResponse;
import vn.motoCare.domain.response.notification.GetNotificationResponse;
import vn.motoCare.repository.NotificationRepository;
import vn.motoCare.repository.UserRepository;
import vn.motoCare.service.NotificationService;
import vn.motoCare.util.enumEntity.EnumTypeNotification;
import vn.motoCare.util.exception.IdInvalidException;

import java.util.List;

@Service
@Slf4j(topic = "NOTIFICATION-SERVICE")
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public CreateNotificationResponse handleCreate(CreateNotificationRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại user này!"));

        NotificationEntity notification = new NotificationEntity();
        notification.setUser(user);
        notification.setTitle(request.getTitle());
        notification.setContent(request.getContent());
        notification.setType(request.getType());

        NotificationEntity saved = notificationRepository.save(notification);
        return toCreateResponse(saved);
    }

    @Override
    public void createNotificationForUser(Long userId, String title, String content, EnumTypeNotification type) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElse(null);
            if (user == null) {
                log.warn("Cannot create notification: User with id {} not found", userId);
                return;
            }

            NotificationEntity notification = new NotificationEntity();
            notification.setUser(user);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setType(type);

            notificationRepository.save(notification);
            log.debug("Notification created for user {}: {}", userId, title);
        } catch (Exception e) {
            log.error("Error creating notification for user {}: {}", userId, e.getMessage());
        }
    }

    @Override
    public void createNotificationForAllUsers(String title, String content, EnumTypeNotification type) {
        try {
            List<UserEntity> users = userRepository.findAll();
            for (UserEntity user : users) {
                NotificationEntity notification = new NotificationEntity();
                notification.setUser(user);
                notification.setTitle(title);
                notification.setContent(content);
                notification.setType(type);

                notificationRepository.save(notification);
            }
            log.debug("Notifications created for all {} users: {}", users.size(), title);
        } catch (Exception e) {
            log.error("Error creating notifications for all users: {}", e.getMessage());
        }
    }

    @Override
    public GetNotificationResponse handleGetByUserId(Long userId) {
        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại user này!"));

        List<NotificationEntity> entities = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        GetNotificationResponse response = new GetNotificationResponse();
        List<GetNotificationResponse.Notification> notifications = entities.stream()
                .map(NotificationServiceImpl::mapToNotification)
                .toList();
        response.setNotifications(notifications);
        return response;
    }

    @Override
    public void handleMarkAsRead(Long id) {
        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại thông báo này!"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void handleDelete(Long id) {
        notificationRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại thông báo này!"));
        notificationRepository.deleteById(id);
    }

    private static CreateNotificationResponse toCreateResponse(NotificationEntity entity) {
        CreateNotificationResponse res = new CreateNotificationResponse();
        res.setId(entity.getId());
        if (entity.getUser() != null) {
            res.setUserId(entity.getUser().getId());
            res.setUserEmail(entity.getUser().getEmail());
        }
        res.setTitle(entity.getTitle());
        res.setContent(entity.getContent());
        res.setType(entity.getType());
        res.setRead(entity.isRead());
        res.setCreatedAt(entity.getCreatedAt());
        return res;
    }

    private static GetNotificationResponse.Notification mapToNotification(NotificationEntity entity) {
        GetNotificationResponse.Notification n = new GetNotificationResponse.Notification();
        n.setId(entity.getId());
        if (entity.getUser() != null) {
            n.setUserId(entity.getUser().getId());
            n.setUserEmail(entity.getUser().getEmail());
        }
        n.setTitle(entity.getTitle());
        n.setContent(entity.getContent());
        n.setType(entity.getType());
        n.setRead(entity.isRead());
        n.setCreatedAt(entity.getCreatedAt());
        return n;
    }
}
