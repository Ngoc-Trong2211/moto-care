package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.service.RoleService;
import vn.motoCare.util.annotation.ApiMessage;

import vn.motoCare.domain.request.role.CreateRoleRequest;
import vn.motoCare.domain.request.role.RoleSpecificationRequest;
import vn.motoCare.domain.request.role.UpdateRoleRequest;
import vn.motoCare.domain.response.role.CreateRoleResponse;
import vn.motoCare.domain.response.role.GetRoleResponse;
import vn.motoCare.domain.response.role.UpdateRoleResponse;
import vn.motoCare.util.exception.IdInvalidException;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "ROLE-CONTROLLER")
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/roles")
    @ApiMessage(message = "Create role")
    public ResponseEntity<CreateRoleResponse> createRole(@Valid @RequestBody CreateRoleRequest req) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.handleCreateRole(req));
    }

    @PutMapping("/roles")
    @ApiMessage(message = "Update role")
    public ResponseEntity<UpdateRoleResponse> updateRole(@Valid @RequestBody UpdateRoleRequest req) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.roleService.handleUpdateRole(req));
    }

    @PatchMapping("/roles/{id}")
    @ApiMessage(message = "Update role active")

    public ResponseEntity<String> patchRoleActive(@PathVariable Long id, @RequestParam Boolean active) throws IdInvalidException {
        this.roleService.handleUpdateActiveRole(id, active);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Cập nhật thành công!");
    }

    @GetMapping("/roles")
    @ApiMessage(message = "Get role")
    public ResponseEntity<GetRoleResponse> getUser(RoleSpecificationRequest req, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.handleGetRole(pageable, req));
    }
}