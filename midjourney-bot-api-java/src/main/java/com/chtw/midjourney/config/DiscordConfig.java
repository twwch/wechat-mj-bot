package com.chtw.midjourney.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "discord")
@Data
public class DiscordConfig {
    private String userToken;
    private String botToken;
    private String serverId;
    private String channelId;
}
