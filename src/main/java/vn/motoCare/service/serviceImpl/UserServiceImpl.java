package vn.motoCare.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.motoCare.domain.RoleEntity;
import vn.motoCare.domain.UserEntity;
import vn.motoCare.domain.request.user.UserChangePassword;
import vn.motoCare.domain.request.user.UserCreateRequest;
import vn.motoCare.domain.request.user.UserSpecificationRequest;
import vn.motoCare.domain.request.user.UserUpdateRequest;
import vn.motoCare.domain.response.user.UserCreateResponse;
import vn.motoCare.domain.response.user.UserGetResponse;
import vn.motoCare.domain.response.user.UserUpdateResponse;
import vn.motoCare.repository.UserRepository;
import vn.motoCare.service.RoleService;
import vn.motoCare.service.UserService;
import vn.motoCare.service.specification.UserSpecification;
import vn.motoCare.util.enumEntity.StatusEnumUser;
import vn.motoCare.util.exception.ChangePasswordException;
import vn.motoCare.util.exception.IdInvalidException;
import vn.motoCare.util.exception.PasswordMismatchException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SERVICE")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public Optional<UserEntity> findById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email)==1;
    }

    @Override
    public UserCreateResponse handleCreateUser(UserCreateRequest req) throws IdInvalidException {
        UserEntity user = new UserEntity();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setStatus(StatusEnumUser.ACTIVE);
        RoleEntity role = this.roleService.findById(req.getRoleId());
        if (role==null) throw new IdInvalidException("Role không tồn tại!");
        user.setRole(role);
        this.userRepository.save(user);
        return convertToCreateResponse(user);
    }

    private UserCreateResponse convertToCreateResponse(UserEntity user){
        UserCreateResponse res = new UserCreateResponse();
        res.setName(user.getName());
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setCreatedAt(user.getCreatedAt());
        res.setCreatedBy(user.getCreatedBy());
        res.setRole(user.getRole().getName());
        return res;
    }

    @Override
    public UserUpdateResponse handleUpdateUser(UserUpdateRequest req) throws IdInvalidException {
        Optional<UserEntity> user = this.findById(req.getId());
        if(user.isPresent()){
            UserEntity currentUser = user.get();
            currentUser.setEmail(req.getEmail());
            currentUser.setName(req.getName());
            RoleEntity role = this.roleService.findById(req.getRoleId());
            if (role==null) throw new IdInvalidException("Role không tồn tại!");
            currentUser.setRole(role);
            this.userRepository.save(currentUser);
            return convertToUpdateResponse(currentUser);
        }
        return null;
    }

    private UserUpdateResponse convertToUpdateResponse(UserEntity user){
        UserUpdateResponse res = new UserUpdateResponse();
        res.setName(user.getName());
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setUpdatedBy(user.getUpdatedBy());
        res.setStatus(user.getStatus().toString());
        res.setRole(user.getRole().getName());
        return res;
    }

    @Override
    public UserGetResponse handleGetUser(Pageable pageable, UserSpecificationRequest req) {
        Specification<UserEntity> spec = UserSpecification.specUser(req);
        Page<UserEntity> pageUser = this.userRepository.findAll(spec, pageable);
        UserGetResponse res = new UserGetResponse();
        UserGetResponse.DataPage resPage = new UserGetResponse.DataPage();

        resPage.setNumber(pageUser.getNumber() + 1);
        resPage.setSize(pageUser.getSize());
        resPage.setNumberOfElements(pageUser.getNumberOfElements());
        resPage.setTotalPages(pageUser.getTotalPages());
        res.setPage(resPage);

        List<UserGetResponse.User> users = pageUser.getContent()
                .stream().map(user -> {
                    UserGetResponse.User resUser = new UserGetResponse.User();
                    resUser.setId(user.getId());
                    resUser.setName(user.getName());
                    resUser.setEmail(user.getEmail());
                    resUser.setUpdatedAt(user.getUpdatedAt());
                    resUser.setUpdatedBy(user.getUpdatedBy());
                    resUser.setCreatedAt(user.getCreatedAt());
                    resUser.setCreatedBy(user.getCreatedBy());
                    resUser.setStatus(user.getStatus().toString());
                    resUser.setRole(user.getRole().getName());
                    return resUser;
                }).toList();
        res.setUsers(users);
        return res;
    }

    @Override
    public UserEntity checkStatusUser(Long id) {
        return this.userRepository.checkStatusUser("INACTIVE", id);
    }

    @Override
    public UserGetResponse.User handleUpdateStatus(Long id) {
        Optional<UserEntity> user = this.findById(id);
        if (user.isPresent()){
            UserEntity currentUser = user.get();
            currentUser.setStatus(StatusEnumUser.INACTIVE);
            this.userRepository.save(currentUser);

            UserGetResponse.User resUser = new UserGetResponse.User();
            resUser.setId(currentUser.getId());
            resUser.setName(currentUser.getName());
            resUser.setEmail(currentUser.getEmail());
            resUser.setUpdatedAt(currentUser.getUpdatedAt());
            resUser.setUpdatedBy(currentUser.getUpdatedBy());
            resUser.setCreatedAt(currentUser.getCreatedAt());
            resUser.setCreatedBy(currentUser.getCreatedBy());
            resUser.setStatus(StatusEnumUser.INACTIVE.toString());
            return resUser;
        }
        return null;
    }

    @Override
    public void handleChangePassword(UserChangePassword req, Long id) throws ChangePasswordException, PasswordMismatchException {
        Optional<UserEntity> user = this.findById(id);
        if (user.isPresent()){
            UserEntity currentUser = user.get();
            if (currentUser.getEmail().equals(req.getEmail())){
                BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
                if (!bCrypt.matches(req.getCurrentPassword(), currentUser.getPassword())){
                    throw new ChangePasswordException("Mật khẩu hiện tại không đúng! Không thể cập nhật mật khẩu");
                }
                else{
                    if(req.getNewPassword().equals(req.getConfirmPassword())){
                        currentUser.setPassword(passwordEncoder.encode(req.getNewPassword()));
                        this.userRepository.save(currentUser);
                    }
                    else throw new PasswordMismatchException("Mật khẩu và xác nhận mật khẩu không giống nhau!");
                }
            }
            else throw new ChangePasswordException("Email không khớp với email bạn đăng kí");
        }
    }
}
