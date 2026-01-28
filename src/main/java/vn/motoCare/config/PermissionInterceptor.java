package vn.motoCare.config;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import vn.motoCare.domain.PermissionEntity;
import vn.motoCare.domain.RoleEntity;
import vn.motoCare.domain.UserEntity;
import vn.motoCare.service.UserService;
import vn.motoCare.service.serviceImpl.AuthServiceImpl;
import vn.motoCare.util.exception.PermissionInvalidException;

import java.util.List;

@Slf4j(topic = "PERMISSION-INTERCEPTOR")
public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        String path = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        log.info("=>>>>>>>>>>{}", requestURI);
        log.info("=>>>>>>>>>>{}", path);
        log.info("=>>>>>>>>>>{}", method);

        String email = AuthServiceImpl.getCurrentUserLogin().isPresent() ? AuthServiceImpl.getCurrentUserLogin().get() : null;
        if (email!=null){
            UserEntity user = this.userService.findByEmail(email).isPresent() ? this.userService.findByEmail(email).get() : null;
            if (user!=null){
                RoleEntity role = user.getRole();
                if (role!=null && role.isActive()){
                    List<PermissionEntity> permissions = role.getPermissions();
                    boolean isAccept = permissions.stream().anyMatch(permission ->
                            permission.getMethod().toString().equals(method) && permission.getPath().equals(path));
                    if (!isAccept) throw new PermissionInvalidException("Bạn không có quyền truy cập");
                }
                else throw new PermissionInvalidException("Bạn không có quyền truy cập");
            }
        }

        return true;
    }
}
