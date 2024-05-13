package com.abdecd.moebackend.business.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OkHttpConfig {
    @Bean
    public OkHttpClient okHttpClient() {
        // todo config
        return new OkHttpClient.Builder().build();
    }
}
