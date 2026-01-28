package vn.motoCare.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import vn.motoCare.service.serviceImpl.AuthServiceImpl;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtConfig {
    @Value("${security.authentication.jwt.base64-secret}")
    private String jwtKey;

    private SecretKey getSecretKey(){
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, AuthServiceImpl.JWT_ALGORITHM.getName());
    }

    @Bean
    private JwtEncoder jwtEncoder(){
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    @Bean
    private JwtDecoder jwtDecoder(){
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(AuthServiceImpl.JWT_ALGORITHM).build();
        return token -> {
            try {
                return nimbusJwtDecoder.decode(token);
            }
            catch (Exception ex){
                System.out.println(">>> JWT error: " + ex.getMessage());
                throw ex;
            }
        };
    }
}
