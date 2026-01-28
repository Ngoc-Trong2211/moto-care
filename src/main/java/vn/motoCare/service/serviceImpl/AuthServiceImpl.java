package vn.motoCare.service.serviceImpl;

import com.nimbusds.jose.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.motoCare.domain.RoleEntity;
import vn.motoCare.domain.UserEntity;
import vn.motoCare.domain.request.auth.LoginRequest;
import vn.motoCare.domain.request.auth.RegisterRequest;
import vn.motoCare.domain.response.auth.LoginResponse;
import vn.motoCare.domain.response.auth.LoginResult;
import vn.motoCare.repository.UserRepository;
import vn.motoCare.service.AuthService;
import vn.motoCare.service.RoleService;
import vn.motoCare.service.UserService;
import vn.motoCare.util.enumEntity.StatusEnumUser;
import vn.motoCare.util.exception.EmailAlreadyExistsException;
import vn.motoCare.util.exception.PasswordMismatchException;
import vn.motoCare.util.exception.RefreshTokenInvalidException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j(topic = "AUTH-SERVICE")
public class AuthServiceImpl implements AuthService {
    private final JwtEncoder jwtEncoder;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${security.authentication.jwt.base64-secret}")
    private String jwtKey;

    @Value("${security.authentication.jwt.access-token-validity}")
    private long accessTokenTime;

    @Value(("${security.authentication.jwt.refresh-token-validity}"))
    private long refreshTokenTime;

    private SecretKey getSecretKey(){
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, AuthServiceImpl.JWT_ALGORITHM.getName());
    }

    public String createToken(String email, LoginResponse.UserLogin user) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenTime, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("motocare", user)
                .build();

        JwsHeader header = JwsHeader.with(JWT_ALGORITHM).build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    @Override
    public String createRefreshToken(String email, LoginResponse.UserLogin user){
        Instant now = Instant.now();
        Instant validity = now.plus(refreshTokenTime, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(email)
                .issuedAt(now)
                .expiresAt(validity)
                .claim("motocare", user.getId())
                .build();

        JwsHeader header = JwsHeader.with(JWT_ALGORITHM).build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    @Override
    public LoginResult loginUser(LoginRequest req) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResult loginResult = new LoginResult();

        LoginResponse loginResponse = new LoginResponse();
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();

        Optional<UserEntity> user = this.userService.findByEmail(req.getEmail());
        UserEntity currentUser = new UserEntity();

        if (user.isPresent()){
            currentUser = user.get();
            if (currentUser.getStatus() == StatusEnumUser.INACTIVE) {
                throw new DisabledException("Tài khoản đã bị vô hiệu hóa");
            }
            else if (currentUser.getStatus() == StatusEnumUser.LOCKED) {
                throw new LockedException("🚫 Tài khoản đã bị khóa do đăng nhập sai quá số lần cho phép." +
                        " Vui lòng liên hệ chúng tôi để khôi phục lại!");
            }
            else{
                userLogin.setId(currentUser.getId());
                userLogin.setName(currentUser.getName());
                userLogin.setEmail(currentUser.getEmail());
                loginResponse.setUserLogin(userLogin);
            }
        }

        String accessToken = this.createToken(req.getEmail(), userLogin);
        loginResponse.setAccessToken(accessToken);

        String refreshToken = this.createRefreshToken(req.getEmail(), userLogin);

        loginResult.setRefreshToken(refreshToken);
        loginResult.setLoginResponse(loginResponse);

        this.userService.handleUpdateRefreshToken(currentUser, refreshToken);
        return loginResult;
    }

    public Jwt checkToken(String token){
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(AuthServiceImpl.JWT_ALGORITHM).build();
        return nimbusJwtDecoder.decode(token);
    }

    @Override
    public LoginResult handleRefreshToken(String refresh) {
        if (refresh.equals("default")) throw new RefreshTokenInvalidException("Refresh token không hợp lệ!");
        LoginResult loginResult = new LoginResult();
        LoginResponse loginResponse = new LoginResponse();
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();

        Jwt jwt = this.checkToken(refresh);
        UserEntity user = this.userService.findUserByEmailAndRefreshToken(jwt.getSubject(), refresh);

        if (user!=null){
            userLogin.setId(user.getId());
            userLogin.setName(user.getName());
            userLogin.setEmail(user.getEmail());
            loginResponse.setUserLogin(userLogin);

            String accessToken = this.createToken(jwt.getSubject(), userLogin);
            loginResponse.setAccessToken(accessToken);

            String refreshToken = this.createRefreshToken(jwt.getSubject(), userLogin);
            this.userService.handleUpdateRefreshToken(user, refreshToken);

            loginResult.setLoginResponse(loginResponse);
            loginResult.setRefreshToken(refreshToken);
        }
        else{
            throw new RefreshTokenInvalidException("Refresh token không hợp lệ!");
        }

        return loginResult;
    }

    @Override
    public void registerUser(RegisterRequest auth) {
        UserEntity user = new UserEntity();
        if (!auth.getNewPassword().equals(auth.getConfirmPassword())) throw new PasswordMismatchException("Mật khẩu và xác nhận mật khẩu không giống nhau!");
        if (this.userService.existsByEmail(auth.getEmail())) {
            throw new EmailAlreadyExistsException("Email đã tồn tại!");
        }
        user.setEmail(auth.getEmail());
        user.setPassword(passwordEncoder.encode(auth.getNewPassword()));
        user.setStatus(StatusEnumUser.ACTIVE);
        RoleEntity role = this.roleService.findByName("USER");
        user.setRole(role);
        this.userRepository.save(user);
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    @Override
    public LoginResponse.UserLogin getAccount() {
        String email = getCurrentUserLogin().isPresent() ? getCurrentUserLogin().get() : "";

        UserEntity user = this.userService.findByEmail(email).isPresent() ? this.userService.findByEmail(email).get() : null;
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();

        if (user != null){
            userLogin.setId(user.getId());
            userLogin.setEmail(user.getEmail());
            userLogin.setName(user.getName());
        }

        return userLogin;
    }

    @Override
    public void handleLogoutUser(String refreshToken) {
        if (refreshToken.equals("default")) throw new RefreshTokenInvalidException("Refresh token không hợp lệ!");

        Jwt jwt = this.checkToken(refreshToken);
        String email = jwt.getSubject();

        String emailLogin = getCurrentUserLogin().isPresent() ? getCurrentUserLogin().get() : "";
        if (emailLogin.isEmpty() || !emailLogin.equals(email)) throw new RefreshTokenInvalidException("Refresh token không hợp lệ!");

        this.userService.findByEmail(email).ifPresent(user -> this.userService.handleUpdateRefreshToken(user, null));
    }
}
