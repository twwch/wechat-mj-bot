package com.chtw.midjourney.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "call-back")
@Data
public class CallBackConfig {
    private String url;
}