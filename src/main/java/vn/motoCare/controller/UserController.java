package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.service.UserService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.domain.UserEntity;
import vn.motoCare.domain.request.user.UserChangePassword;
import vn.motoCare.domain.request.user.UserCreateRequest;
import vn.motoCare.domain.request.user.UserSpecificationRequest;
import vn.motoCare.domain.request.user.UserUpdateRequest;
import vn.motoCare.domain.response.user.UserCreateResponse;
import vn.motoCare.domain.response.user.UserGetResponse;
import vn.motoCare.domain.response.user.UserUpdateResponse;
import vn.motoCare.util.exception.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "USER-CONTROLLER")
@RequestMapping("/user/v1")
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    @ApiMessage(message = "Create user")
    public ResponseEntity<UserCreateResponse> createUser(@Valid @RequestBody UserCreateRequest req) throws EmailAlreadyExistsException, IdInvalidException {
        if (this.userService.findByEmail(req.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email đã tồn tại!");
        }
        log.info("Email hợp lệ");
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.handleCreateUser(req));
    }

    @PutMapping("/users")
    @ApiMessage(message = "Update user")
    public ResponseEntity<UserUpdateResponse> updateUser(@Valid @RequestBody UserUpdateRequest req){
        UserEntity user = this.userService.findById(req.getId()).isPresent() ? this.userService.findById(req.getId()).get() : null;
        if(user==null){
            throw new IdInvalidException("Id không tồn tại!");
        }
        if (this.userService.existsByEmail(req.getEmail()) && !Objects.equals(user.getEmail(), req.getEmail())) {
            throw new EmailAlreadyExistsException("Email đã tồn tại!");
        }
        log.info("Id, Email hợp lệ");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.userService.handleUpdateUser(req));
    }

    @GetMapping("/users")
    @ApiMessage(message = "Get user")
    public ResponseEntity<UserGetResponse> getUser(UserSpecificationRequest req, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetUser(pageable, req));
    }

    @PatchMapping("/users/{id}")
    @ApiMessage(message = "Disable user")
    public ResponseEntity<UserGetResponse.User> disableUser(@PathVariable("id") Long id) throws IdInvalidException, StatusIsActiveException {
        if(this.userService.findById(id).isEmpty()){
            throw new IdInvalidException("Id không tồn tại!");
        }
        if(this.userService.checkStatusUser(id)!=null){
            throw new StatusIsActiveException("Người dùng này đã vô hiệu hóa trước đó");
        }
        log.info("Id hợp lệ");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.userService.handleUpdateStatus(id));
    }

    @PatchMapping("/users/change-password/{id}")
    @ApiMessage(message = "Change password")
    public ResponseEntity<String> changePassword(@PathVariable("id") Long id, UserChangePassword req) throws IdInvalidException, ChangePasswordException, PasswordMismatchException {
        if(this.userService.findById(id).isEmpty()){
            throw new IdInvalidException("Id không tồn tại!");
        }
        log.info("Id hợp lệ!");
        this.userService.handleChangePassword(req, id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Đã cập nhật mật khẩu mới");
    }
}
