package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.motoCare.service.MaintenanceReminderService;

@Service
@Slf4j(topic = "SCHEDULED-TASK-SERVICE")
@RequiredArgsConstructor
public class ScheduledTaskService {
    private final MaintenanceReminderService maintenanceReminderService;

    @Scheduled(cron = "0 0 9 * * ?") // Run daily at 9 AM
    public void checkDueMaintenanceReminders() {
        log.info("Running scheduled task: Checking due maintenance reminders");
        try {
            maintenanceReminderService.checkAndNotifyDueMaintenanceReminders();
        } catch (Exception e) {
            log.error("Error in scheduled task for maintenance reminders: {}", e.getMessage(), e);
        }
    }
}
