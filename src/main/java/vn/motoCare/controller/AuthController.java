package vn.motoCare.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.motoCare.domain.request.auth.LoginRequest;
import vn.motoCare.domain.request.auth.RegisterRequest;
import vn.motoCare.domain.response.auth.LoginResponse;
import vn.motoCare.domain.response.auth.LoginResult;
import vn.motoCare.service.AuthService;
import vn.motoCare.util.annotation.ApiMessage;
import vn.motoCare.util.exception.EmailAlreadyExistsException;
import vn.motoCare.util.exception.PasswordMismatchException;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "AUTH-CONTROLLER")
public class AuthController {
    private final AuthService authService;

    @Value(("${security.authentication.jwt.refresh-token-validity}"))
    private long refreshTokenTime;

    @PostMapping("/auth/login")
    @ApiMessage(message = "Login success")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest login){
        LoginResult loginResult = this.authService.loginUser(login);

        ResponseCookie responseCookie = ResponseCookie
                .from("refresh", loginResult.getRefreshToken())
                .httpOnly(true)
                .maxAge(refreshTokenTime)
                .path("/")
                .secure(true)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResult.getLoginResponse());
    }

    @PostMapping("/auth/refresh")
    @ApiMessage(message = "Refresh Token")
    public ResponseEntity<LoginResponse> refreshToken(@CookieValue(name = "refresh", defaultValue = "default") String refresh){
        LoginResult loginResult = this.authService.handleRefreshToken(refresh);

        ResponseCookie responseCookie = ResponseCookie
                .from("refresh", loginResult.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .maxAge(refreshTokenTime)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResult.getLoginResponse());
    }

    @PostMapping("/auth/register")
    @ApiMessage(message = "Register success")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest auth) throws PasswordMismatchException, EmailAlreadyExistsException {
        log.info("Đăng ký tạo mới người dùng");
        this.authService.registerUser(auth);
        return ResponseEntity.status(HttpStatus.CREATED).body("Đăng ký thành công!");
    }

    @GetMapping("/auth/account")
    @ApiMessage(message = "Get account")
    public ResponseEntity<LoginResponse.UserLogin> getAccount(){
        return ResponseEntity.ok().body(this.authService.getAccount());
    }

    @PostMapping("/auth/logout")
    @ApiMessage(message = "Logout success")
    public ResponseEntity<Void> logoutUser(@CookieValue(value = "refresh", defaultValue = "default") String refreshToken){
        this.authService.handleLogoutUser(refreshToken);

        ResponseCookie responseCookie = ResponseCookie
                .from("refresh", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(null);
    }
}
