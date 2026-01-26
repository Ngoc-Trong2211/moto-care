package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.permission.CreatePermissionRequest;
import vn.motoCare.domain.request.permission.PermissionSpecificationRequest;
import vn.motoCare.domain.request.permission.UpdatePermissionRequest;
import vn.motoCare.domain.response.permssion.CreatePermissionResponse;
import vn.motoCare.domain.response.permssion.GetPermissionResponse;
import vn.motoCare.domain.response.permssion.UpdatePermissionResponse;
import vn.motoCare.service.PermissionService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@Slf4j(topic = "PERMISSION-CONTROLLER")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping("/permissions")
    @ApiMessage(message = "Tạo permission thành công")
    public ResponseEntity<CreatePermissionResponse> createPermission(@Valid @RequestBody CreatePermissionRequest req) throws IdInvalidException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(permissionService.handleCreatePermission(req));
    }

    @PutMapping("/permissions")
    @ApiMessage(message = "Cập nhật permission thành công")
    public ResponseEntity<UpdatePermissionResponse> updatePermission(
            @Valid @RequestBody UpdatePermissionRequest req
    ) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(permissionService.handleUpdatePermission(req));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage(message = "Xóa permission thành công")
    public ResponseEntity<String> deletePermission(@PathVariable("id") Long id) throws IdInvalidException{
        this.permissionService.handleDeletePermission(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Xóa permission thành công");
    }

    @GetMapping("/permissions")
    @ApiMessage(message = "Xem danh sách quyền hạn")
    public ResponseEntity<GetPermissionResponse> getPermission(Pageable pageable, @RequestBody PermissionSpecificationRequest req){
        return ResponseEntity.ok(this.permissionService.handleGetPermission(pageable, req));
    }
}