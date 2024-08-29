package com.abdecd.moebackend.business.tokenLogin.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "token-login")
@Data
public class TokenLoginProp {
    private Integer jwtTtlSeconds;
    /**
     不拦截的请求，如可以这样配置：
     - /error
     - /doc.html/**
     - /swagger-ui.html/**
     - /v3/api-docs/**
     - /webjars/**
     */
    private String[] excludePatterns = new String[0];
    /**
     * 测试模式; true 时启用虚拟用户允许所有请求 详见 interceptor/LoginInterceptor.inTest()
     */
    private Boolean test = false;
}
