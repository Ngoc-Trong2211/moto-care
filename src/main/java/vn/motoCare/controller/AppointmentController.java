package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.appointment.AppointmentSpecificationRequest;
import vn.motoCare.domain.request.appointment.CreateAppointmentRequest;
import vn.motoCare.domain.request.appointment.UpdateAppointmentRequest;
import vn.motoCare.domain.request.appointment.UpdateAppointmentStatusRequest;
import vn.motoCare.domain.response.appointment.CreateAppointmentResponse;
import vn.motoCare.domain.response.appointment.GetAppointmentResponse;
import vn.motoCare.domain.response.appointment.UpdateAppointmentResponse;
import vn.motoCare.domain.response.appointment.UpdateAppointmentStatusResponse;
import vn.motoCare.service.AppointmentService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "APPOINTMENT-CONTROLLER")
@RequestMapping("/api/v1")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping("/appointments")
    @ApiMessage(message = "Tạo lịch hẹn thành công")
    public ResponseEntity<CreateAppointmentResponse> createAppointment(
            @RequestBody @Valid CreateAppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.handleCreate(request));
    }

    @GetMapping("/appointments")
    @ApiMessage(message = "Lấy danh sách lịch hẹn thành công")
    public ResponseEntity<GetAppointmentResponse> getAppointments(Pageable pageable, AppointmentSpecificationRequest req) {
        return ResponseEntity.ok(appointmentService.handleGetAppointments(pageable, req));
    }

    @PutMapping("/appointments")
    @ApiMessage(message = "Cập nhật lịch hẹn thành công")
    public ResponseEntity<UpdateAppointmentResponse> updateAppointment(
            @RequestBody @Valid UpdateAppointmentRequest request) throws IdInvalidException {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(appointmentService.handleUpdate(request));
    }

    @PatchMapping("/appointments/{id}/status")
    @ApiMessage(message = "Cập nhật trạng thái lịch hẹn thành công")
    public ResponseEntity<UpdateAppointmentStatusResponse> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateAppointmentStatusRequest request) throws IdInvalidException {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(appointmentService.handleUpdateStatus(id, request));
    }

    @DeleteMapping("/appointments/{id}")
    @ApiMessage(message = "Xóa lịch hẹn thành công")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) throws IdInvalidException {
        appointmentService.handleDelete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Xóa thành công");
    }
}
