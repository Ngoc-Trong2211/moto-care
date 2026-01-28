package vn.motoCare.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.motoCare.domain.UserEntity;
import vn.motoCare.repository.UserRepository;
import vn.motoCare.service.UserService;
import vn.motoCare.util.enumEntity.StatusEnumUser;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserEntity user = this.userService.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Email/Password không tồn tại!"));

        if (user.getStatus() == StatusEnumUser.INACTIVE) {
            throw new DisabledException("Tài khoản đã bị vô hiệu hóa");
        }

        if (user.getStatus() == StatusEnumUser.LOCKED) {
            throw new LockedException("🚫 Tài khoản đã bị khóa do đăng nhập sai quá số lần cho phép." +
                    " Vui lòng liên hệ chúng tôi để khôi phục lại!");
        }

        if (!passwordEncoder.matches(password, user.getPassword())){
            int tries = this.userService.increaseFail(user);
            throw new BadCredentialsException("⚠️ Bạn đã nhập sai thông tin đăng nhập.\n" +
                    "Còn " + (5-tries) + " lần thử trước khi tài khoản bị khóa tạm thời.");
        }

        user.setFailedTry(0);
        this.userRepository.save(user);
        return new UsernamePasswordAuthenticationToken(
                email, null,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()))
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
