package vn.motoCare.service;

import vn.motoCare.domain.request.auth.LoginRequest;
import vn.motoCare.domain.request.auth.RegisterRequest;
import vn.motoCare.domain.response.auth.LoginResponse;
import vn.motoCare.domain.response.auth.LoginResult;

public interface AuthService {
    String createRefreshToken(String email, LoginResponse.UserLogin user);
    LoginResult loginUser(LoginRequest req);
    LoginResult handleRefreshToken(String refresh);
    void registerUser(RegisterRequest req);
    LoginResponse.UserLogin getAccount();
    void handleLogoutUser(String refreshToken);
}
