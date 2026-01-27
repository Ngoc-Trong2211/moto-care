package vn.motoCare.service;

import org.springframework.data.domain.Pageable;
import vn.motoCare.domain.UserEntity;
import vn.motoCare.domain.request.user.UserChangePassword;
import vn.motoCare.domain.request.user.UserCreateRequest;
import vn.motoCare.domain.request.user.UserSpecificationRequest;
import vn.motoCare.domain.request.user.UserUpdateRequest;
import vn.motoCare.domain.response.user.UserCreateResponse;
import vn.motoCare.domain.response.user.UserGetResponse;
import vn.motoCare.domain.response.user.UserUpdateResponse;
import vn.motoCare.util.exception.ChangePasswordException;
import vn.motoCare.util.exception.IdInvalidException;
import vn.motoCare.util.exception.PasswordMismatchException;

import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findByEmail(String email);
    UserCreateResponse handleCreateUser(UserCreateRequest req) throws IdInvalidException;
    UserUpdateResponse handleUpdateUser(UserUpdateRequest req) throws IdInvalidException;
    Optional<UserEntity> findById(Long id);
    UserGetResponse handleGetUser(Pageable pageable, UserSpecificationRequest req);
    UserGetResponse.User handleUpdateStatus(Long id);
    UserEntity checkStatusUser(Long id);
    void handleChangePassword(UserChangePassword req, Long id) throws ChangePasswordException, PasswordMismatchException;
    boolean existsByEmail(String email);
}
