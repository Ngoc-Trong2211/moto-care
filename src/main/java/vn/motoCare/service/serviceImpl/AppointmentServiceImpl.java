package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.motoCare.domain.AgencyEntity;
import vn.motoCare.domain.AppointmentEntity;
import vn.motoCare.domain.UserEntity;
import vn.motoCare.domain.VehicleEntity;
import vn.motoCare.domain.request.appointment.AppointmentSpecificationRequest;
import vn.motoCare.domain.request.appointment.CreateAppointmentRequest;
import vn.motoCare.domain.request.appointment.UpdateAppointmentRequest;
import vn.motoCare.domain.request.appointment.UpdateAppointmentStatusRequest;
import vn.motoCare.domain.response.appointment.CreateAppointmentResponse;
import vn.motoCare.domain.response.appointment.GetAppointmentResponse;
import vn.motoCare.domain.response.appointment.UpdateAppointmentResponse;
import vn.motoCare.domain.response.appointment.UpdateAppointmentStatusResponse;
import vn.motoCare.repository.AgencyRepository;
import vn.motoCare.repository.AppointmentRepository;
import vn.motoCare.repository.UserRepository;
import vn.motoCare.repository.VehicleRepository;
import vn.motoCare.service.AppointmentService;
import vn.motoCare.service.NotificationService;
import vn.motoCare.service.specification.AppointmentSpecification;
import vn.motoCare.util.enumEntity.EnumAppointmentStatus;
import vn.motoCare.util.enumEntity.EnumTypeNotification;
import vn.motoCare.util.exception.IdInvalidException;

import java.util.List;

@Service
@Slf4j(topic = "APPOINTMENT-SERVICE")
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final AgencyRepository agencyRepository;
    private final NotificationService notificationService;

    @Override
    public CreateAppointmentResponse handleCreate(CreateAppointmentRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại user này!"));
        VehicleEntity vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại xe này!"));
        AgencyEntity agency = agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại agency này!"));

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setUser(user);
        appointment.setVehicle(vehicle);
        appointment.setAgency(agency);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStatus(request.getStatus());

        AppointmentEntity saved = appointmentRepository.save(appointment);
        return toCreateResponse(saved);
    }

    private static CreateAppointmentResponse toCreateResponse(AppointmentEntity entity) {
        CreateAppointmentResponse res = new CreateAppointmentResponse();
        res.setId(entity.getId());
        if (entity.getUser() != null) {
            res.setUserId(entity.getUser().getId());
            res.setUserEmail(entity.getUser().getEmail());
        }
        if (entity.getVehicle() != null) {
            res.setVehicleId(entity.getVehicle().getId());
            res.setVehicleLicensePlate(entity.getVehicle().getLicensePlate());
        }
        if (entity.getAgency() != null) {
            res.setAgencyId(entity.getAgency().getId());
            res.setAgencyName(entity.getAgency().getName());
        }
        res.setAppointmentDate(entity.getAppointmentDate());
        res.setStatus(entity.getStatus());
        res.setCreatedAt(entity.getCreatedAt());
        res.setCreatedBy(entity.getCreatedBy());
        return res;
    }

    @Override
    public GetAppointmentResponse handleGetAppointments(Pageable pageable, AppointmentSpecificationRequest req) {
        Specification<AppointmentEntity> spec = AppointmentSpecification.specAppointment(req);
        Page<AppointmentEntity> pageData = appointmentRepository.findAll(spec, pageable);

        GetAppointmentResponse response = new GetAppointmentResponse();
        response.setPage(
                new GetAppointmentResponse.DataPage(
                        pageData.getNumber(),
                        pageData.getSize(),
                        pageData.getNumberOfElements(),
                        pageData.getTotalPages()
                )
        );

        List<GetAppointmentResponse.Appointment> list = pageData.getContent()
                .stream()
                .map(AppointmentServiceImpl::mapToAppointment)
                .toList();
        response.setAppointments(list);

        return response;
    }

    private static GetAppointmentResponse.Appointment mapToAppointment(AppointmentEntity entity) {
        GetAppointmentResponse.Appointment a = new GetAppointmentResponse.Appointment();
        a.setId(entity.getId());
        if (entity.getUser() != null) {
            a.setUserId(entity.getUser().getId());
            a.setUserEmail(entity.getUser().getEmail());
        }
        if (entity.getVehicle() != null) {
            a.setVehicleId(entity.getVehicle().getId());
            a.setVehicleLicensePlate(entity.getVehicle().getLicensePlate());
        }
        if (entity.getAgency() != null) {
            a.setAgencyId(entity.getAgency().getId());
            a.setAgencyName(entity.getAgency().getName());
        }
        a.setAppointmentDate(entity.getAppointmentDate());
        a.setStatus(entity.getStatus());
        a.setCreatedAt(entity.getCreatedAt());
        a.setCreatedBy(entity.getCreatedBy());
        return a;
    }

    @Override
    public UpdateAppointmentResponse handleUpdate(UpdateAppointmentRequest request) {
        AppointmentEntity appointment = appointmentRepository.findById(request.getId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại lịch hẹn này!"));
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại user này!"));
        VehicleEntity vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại xe này!"));
        AgencyEntity agency = agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new IdInvalidException("Không tồn tại agency này!"));

        EnumAppointmentStatus oldStatus = appointment.getStatus();
        appointment.setUser(user);
        appointment.setVehicle(vehicle);
        appointment.setAgency(agency);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStatus(request.getStatus());

        AppointmentEntity saved = appointmentRepository.save(appointment);
        
        // Create notification when status changes
        if (oldStatus != request.getStatus() && saved.getUser() != null) {
            Long userId = saved.getUser().getId();
            String title = "Appointment Status Update";
            String content = "Your appointment for vehicle " + 
                    (saved.getVehicle() != null ? saved.getVehicle().getLicensePlate() : "") +
                    " has been updated to " + request.getStatus().name();
            notificationService.createNotificationForUser(userId, title, content, EnumTypeNotification.SYSTEM);
        }
        
        return toUpdateResponse(saved);
    }

    private static UpdateAppointmentResponse toUpdateResponse(AppointmentEntity entity) {
        UpdateAppointmentResponse res = new UpdateAppointmentResponse();
        res.setId(entity.getId());
        if (entity.getUser() != null) {
            res.setUserId(entity.getUser().getId());
            res.setUserEmail(entity.getUser().getEmail());
        }
        if (entity.getVehicle() != null) {
            res.setVehicleId(entity.getVehicle().getId());
            res.setVehicleLicensePlate(entity.getVehicle().getLicensePlate());
        }
        if (entity.getAgency() != null) {
            res.setAgencyId(entity.getAgency().getId());
            res.setAgencyName(entity.getAgency().getName());
        }
        res.setAppointmentDate(entity.getAppointmentDate());
        res.setStatus(entity.getStatus());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setUpdatedBy(entity.getUpdatedBy());
        return res;
    }

    @Override
    public UpdateAppointmentStatusResponse handleUpdateStatus(Long id, UpdateAppointmentStatusRequest request) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại lịch hẹn này!"));
        
        EnumAppointmentStatus oldStatus = appointment.getStatus();
        appointment.setStatus(request.getStatus());
        AppointmentEntity saved = appointmentRepository.save(appointment);
        
        // Create notification when status changes
        if (oldStatus != request.getStatus() && saved.getUser() != null) {
            Long userId = saved.getUser().getId();
            String title = "Appointment Status Update";
            String content = "Your appointment for vehicle " + 
                    (saved.getVehicle() != null ? saved.getVehicle().getLicensePlate() : "") +
                    " status has been changed to " + request.getStatus().name();
            notificationService.createNotificationForUser(userId, title, content, EnumTypeNotification.SYSTEM);
        }
        
        return toUpdateStatusResponse(saved);
    }

    private static UpdateAppointmentStatusResponse toUpdateStatusResponse(AppointmentEntity entity) {
        UpdateAppointmentStatusResponse res = new UpdateAppointmentStatusResponse();
        res.setId(entity.getId());
        res.setStatus(entity.getStatus());
        res.setUpdatedAt(entity.getUpdatedAt());
        res.setUpdatedBy(entity.getUpdatedBy());
        return res;
    }

    @Override
    public void handleDelete(Long id) {
        appointmentRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tồn tại lịch hẹn này!"));
        appointmentRepository.deleteById(id);
    }
}
