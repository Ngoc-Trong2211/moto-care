package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.motoCare.domain.MaintenanceReminderEntity;
import vn.motoCare.domain.MaintenanceTypeEntity;
import vn.motoCare.domain.VehicleEntity;
import vn.motoCare.domain.request.maintenanceReminder.CreateMaintenanceReminderRequest;
import vn.motoCare.domain.request.maintenanceReminder.UpdateMaintenanceReminderStatusRequest;
import vn.motoCare.domain.response.maintenanceReminder.CreateMaintenanceReminderResponse;
import vn.motoCare.domain.response.maintenanceReminder.GetMaintenanceReminderResponse;
import vn.motoCare.domain.response.maintenanceReminder.UpdateMaintenanceReminderStatusResponse;
import vn.motoCare.repository.MaintenanceReminderRepository;
import vn.motoCare.repository.MaintenanceTypeRepository;
import vn.motoCare.repository.VehicleRepository;
import vn.motoCare.service.MaintenanceReminderService;
import vn.motoCare.service.NotificationService;
import vn.motoCare.util.enumEntity.EnumStatusReminder;
import vn.motoCare.util.enumEntity.EnumTypeNotification;
import vn.motoCare.util.exception.IdInvalidException;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j(topic = "MAINTENANCE-REMINDER-SERVICE")
@RequiredArgsConstructor
@Transactional
public class MaintenanceReminderServiceImpl implements MaintenanceReminderService {
    private final MaintenanceReminderRepository maintenanceReminderRepository;
    private final VehicleRepository vehicleRepository;
    private final MaintenanceTypeRepository maintenanceTypeRepository;
    private final NotificationService notificationService;

    @Override
    public CreateMaintenanceReminderResponse handleCreate(CreateMaintenanceReminderRequest request) {
        VehicleEntity vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại xe này!"));
        MaintenanceTypeEntity maintenanceType = maintenanceTypeRepository.findById(request.getMaintenanceTypeId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại loại bảo dưỡng này!"));

        MaintenanceReminderEntity reminder = new MaintenanceReminderEntity();
        reminder.setVehicle(vehicle);
        reminder.setMaintenanceType(maintenanceType);
        reminder.setDueDate(request.getDueDate());
        reminder.setStatus(request.getStatus());

        MaintenanceReminderEntity saved = maintenanceReminderRepository.save(reminder);
        return toCreateResponse(saved);
    }

    @Override
    public CreateMaintenanceReminderResponse handleGetById(Long id) {
        MaintenanceReminderEntity entity = maintenanceReminderRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại nhắc nhở bảo dưỡng này!"));
        return toCreateResponse(entity);
    }

    @Override
    public GetMaintenanceReminderResponse handleGetByVehicleId(Long vehicleId) {
        // Validate vehicle exists
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại xe này!"));

        List<MaintenanceReminderEntity> entities = maintenanceReminderRepository.findByVehicleId(vehicleId);
        GetMaintenanceReminderResponse response = new GetMaintenanceReminderResponse();
        List<GetMaintenanceReminderResponse.MaintenanceReminder> reminders = entities.stream()
                .map(MaintenanceReminderServiceImpl::mapToMaintenanceReminder)
                .toList();
        response.setMaintenanceReminders(reminders);
        return response;
    }

    @Override
    public UpdateMaintenanceReminderStatusResponse handleUpdateStatus(Long id, UpdateMaintenanceReminderStatusRequest request) {
        MaintenanceReminderEntity reminder = maintenanceReminderRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại nhắc nhở bảo dưỡng này!"));

        EnumStatusReminder oldStatus = reminder.getStatus();
        // Only update status, do not update other fields
        reminder.setStatus(request.getStatus());

        MaintenanceReminderEntity saved = maintenanceReminderRepository.save(reminder);
        
        // Create notification when status changes from PENDING to SENT or DONE
        if (oldStatus == EnumStatusReminder.PENDING && 
            (request.getStatus() == EnumStatusReminder.SENT || request.getStatus() == EnumStatusReminder.DONE)) {
            if (saved.getVehicle() != null && saved.getVehicle().getUser() != null) {
                Long userId = saved.getVehicle().getUser().getId();
                String title = "Maintenance Reminder";
                String content = "Your vehicle " + saved.getVehicle().getLicensePlate() + 
                        " maintenance reminder status has been updated to " + request.getStatus().name();
                notificationService.createNotificationForUser(userId, title, content, EnumTypeNotification.MAINTENANCE);
            }
        }
        
        return toUpdateStatusResponse(saved);
    }
    
    /**
     * Check for maintenance reminders that have reached their due date and create notifications
     * This method should be called by a scheduled task
     */
    public void checkAndNotifyDueMaintenanceReminders() {
        LocalDate today = LocalDate.now();
        List<MaintenanceReminderEntity> dueReminders = maintenanceReminderRepository.findAll().stream()
                .filter(reminder -> reminder.getDueDate() != null 
                        && reminder.getDueDate().equals(today)
                        && reminder.getStatus() == EnumStatusReminder.PENDING)
                .toList();
        
        for (MaintenanceReminderEntity reminder : dueReminders) {
            if (reminder.getVehicle() != null && reminder.getVehicle().getUser() != null) {
                Long userId = reminder.getVehicle().getUser().getId();
                String title = "Maintenance Reminder";
                String content = "Your vehicle " + reminder.getVehicle().getLicensePlate() + 
                        " is due for " + (reminder.getMaintenanceType() != null ? reminder.getMaintenanceType().getName() : "maintenance");
                notificationService.createNotificationForUser(userId, title, content, EnumTypeNotification.MAINTENANCE);
            }
        }
    }

    @Override
    public void handleDelete(Long id) {
        maintenanceReminderRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại nhắc nhở bảo dưỡng này!"));
        maintenanceReminderRepository.deleteById(id);
    }

    private static CreateMaintenanceReminderResponse toCreateResponse(MaintenanceReminderEntity entity) {
        CreateMaintenanceReminderResponse res = new CreateMaintenanceReminderResponse();
        res.setId(entity.getId());
        if (entity.getVehicle() != null) {
            res.setVehicleId(entity.getVehicle().getId());
            res.setVehicleLicensePlate(entity.getVehicle().getLicensePlate());
        }
        if (entity.getMaintenanceType() != null) {
            res.setMaintenanceTypeId(entity.getMaintenanceType().getId());
            res.setMaintenanceTypeName(entity.getMaintenanceType().getName());
        }
        res.setDueDate(entity.getDueDate());
        res.setStatus(entity.getStatus());
        res.setCreatedAt(entity.getCreatedAt());
        res.setCreatedBy(entity.getCreatedBy());
        return res;
    }

    private static GetMaintenanceReminderResponse.MaintenanceReminder mapToMaintenanceReminder(MaintenanceReminderEntity entity) {
        GetMaintenanceReminderResponse.MaintenanceReminder r = new GetMaintenanceReminderResponse.MaintenanceReminder();
        r.setId(entity.getId());
        if (entity.getVehicle() != null) {
            r.setVehicleId(entity.getVehicle().getId());
            r.setVehicleLicensePlate(entity.getVehicle().getLicensePlate());
        }
        if (entity.getMaintenanceType() != null) {
            r.setMaintenanceTypeId(entity.getMaintenanceType().getId());
            r.setMaintenanceTypeName(entity.getMaintenanceType().getName());
        }
        r.setDueDate(entity.getDueDate());
        r.setStatus(entity.getStatus());
        r.setCreatedAt(entity.getCreatedAt());
        r.setCreatedBy(entity.getCreatedBy());
        return r;
    }

    private static UpdateMaintenanceReminderStatusResponse toUpdateStatusResponse(MaintenanceReminderEntity entity) {
        UpdateMaintenanceReminderStatusResponse res = new UpdateMaintenanceReminderStatusResponse();
        res.setId(entity.getId());
        res.setStatus(entity.getStatus());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setUpdatedBy(entity.getUpdatedBy());
        return res;
    }
}
