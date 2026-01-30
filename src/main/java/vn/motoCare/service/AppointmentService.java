package vn.motoCare.service;

import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.request.appointment.AppointmentSpecificationRequest;
import vn.motoCare.domain.request.appointment.CreateAppointmentRequest;
import vn.motoCare.domain.request.appointment.UpdateAppointmentRequest;
import vn.motoCare.domain.request.appointment.UpdateAppointmentStatusRequest;
import vn.motoCare.domain.response.appointment.CreateAppointmentResponse;
import vn.motoCare.domain.response.appointment.GetAppointmentResponse;
import vn.motoCare.domain.response.appointment.UpdateAppointmentResponse;
import vn.motoCare.domain.response.appointment.UpdateAppointmentStatusResponse;

public interface AppointmentService {
    CreateAppointmentResponse handleCreate(CreateAppointmentRequest request);
    GetAppointmentResponse handleGetAppointments(Pageable pageable, AppointmentSpecificationRequest req);
    UpdateAppointmentResponse handleUpdate(UpdateAppointmentRequest request);
    UpdateAppointmentStatusResponse handleUpdateStatus(Long id, UpdateAppointmentStatusRequest request);
    void handleDelete(Long id);
}
