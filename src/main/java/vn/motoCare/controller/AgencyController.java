package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.agency.AgencySpecificationRequest;
import vn.motoCare.domain.request.agency.CreateAgencyRequest;
import vn.motoCare.domain.request.agency.UpdateAgencyRequest;
import vn.motoCare.domain.response.agency.CreateAgencyResponse;
import vn.motoCare.domain.response.agency.GetAgencyResponse;
import vn.motoCare.domain.response.agency.UpdateAgencyResponse;
import vn.motoCare.service.AgencyService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@Slf4j(topic = "AGENCY-CONTROLLER")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AgencyController {
    private final AgencyService agencyService;

    @PostMapping("/agencies")
    @ApiMessage(message = "create success")
    public ResponseEntity<CreateAgencyResponse> createAgency(@Valid @RequestBody CreateAgencyRequest request) {
        return new ResponseEntity<>(this.agencyService.handleCreate(request), HttpStatus.CREATED);
    }

    @PutMapping("/agencies")
    @ApiMessage(message = "Cập nhật agency thành công")
    public ResponseEntity<UpdateAgencyResponse> updateAgency(
            @Valid @RequestBody UpdateAgencyRequest req
    ) throws IdInvalidException {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(this.agencyService.handleUpdateAgency(req));
    }

    @GetMapping("/agencies")
    @ApiMessage(message = "Lấy danh sách agency thành công")
    public ResponseEntity<GetAgencyResponse> getAgencies(Pageable pageable, AgencySpecificationRequest req) {
        return ResponseEntity.ok(this.agencyService.handleGetAgency(pageable, req));
    }

    @DeleteMapping("/agencies/{id}")
    @ApiMessage(message = "Xoá (vô hiệu hoá) agency thành công")
    public ResponseEntity<String> deleteAgency(@PathVariable Long id) throws IdInvalidException {
        this.agencyService.handleDeleteAgency(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Xóa thành công");
    }

    @PatchMapping("/agencies/{id}/active")
    @ApiMessage(message = "Cập nhật trạng thái agency thành công")
    public ResponseEntity<String> updateAgencyActive(@PathVariable Long id) throws IdInvalidException {
        this.agencyService.handleUpdateAgencyActive(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Cập nhật thành công");
    }
}
