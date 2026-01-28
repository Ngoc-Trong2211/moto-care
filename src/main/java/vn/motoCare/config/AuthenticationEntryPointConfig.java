package vn.motoCare.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import vn.motoCare.domain.response.ResponseSystem;

import java.io.IOException;

@Component
public class AuthenticationEntryPointConfig implements AuthenticationEntryPoint {
    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

    private final ObjectMapper mapper;

    public AuthenticationEntryPointConfig(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        this.delegate.commence(request, response, authException);
        response.setContentType("application/json;charset=UTF-8");

        ResponseSystem<Object> rs = new ResponseSystem<>();
        rs.setStatus(HttpStatus.UNAUTHORIZED.value());
        rs.setMessage("Token không hợp lệ! Vui lòng kiểm tra lại");

        mapper.writeValue(response.getWriter(), rs);
    }
}
