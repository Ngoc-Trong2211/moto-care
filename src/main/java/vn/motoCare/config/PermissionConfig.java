package vn.motoCare.config;

import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionConfig implements WebMvcConfigurer {
    @Bean
    PermissionInterceptor permissionInterceptor(){
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        String[] whiteList = {"/", "/api/v1/auth/**"};
        registry.addInterceptor(permissionInterceptor()).excludePathPatterns(whiteList);
    }
}
