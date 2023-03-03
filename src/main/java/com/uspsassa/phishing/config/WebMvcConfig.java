package com.uspsassa.phishing.config;

import com.uspsassa.phishing.interceptor.SecurityInterceptor;
import com.uspsassa.phishing.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 安全拦截
        SecurityInterceptor securityInterceptor = new SecurityInterceptor();
        // Token拦截
        TokenInterceptor tokenInterceptor = new TokenInterceptor();
        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/user")
                .addPathPatterns("/admin")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/v3/**");
        // Token拦截
        // Admin才需要token拦截
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/admin/**").excludePathPatterns("/admin/login");
    }
}
