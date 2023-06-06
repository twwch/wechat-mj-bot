package com.chtw.midjourney.service.impl;

import com.chtw.midjourney.config.MidjourneyBotConfiguration;
import com.chtw.midjourney.request.*;
import com.chtw.midjourney.service.DiscordInteractionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.util.Arrays;

@Service
@Slf4j
public class DiscordInteractionServiceImpl implements DiscordInteractionService {
    private static final String DISCORD_API_URL = "https://discord.com/api/v9/interactions";
    private final WebClient webClient;

    private final MidjourneyBotConfiguration midjourneyBotConfiguration;
    @Autowired
    public DiscordInteractionServiceImpl(MidjourneyBotConfiguration midjourneyBotConfiguration,
                                         WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.build();
        // webClient设置代理
        this.webClient = webClientBuilder.clientConnector(
                new ReactorClientHttpConnector(HttpClient.create()
                        .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
                                .host("127.0.0.1")
                                .port(7890)
                        )))
                .build();
        this.midjourneyBotConfiguration= midjourneyBotConfiguration;
    }

    /**
     * Call GenerateImage service
     * @param prompt
     */
    @Override
    public void generateImage(String prompt) {
        sendRequest(RequestMidjourneyDto.builder()
                .guild_id(midjourneyBotConfiguration.getDiscordConfig().getServerId())
                .channel_id(midjourneyBotConfiguration.getDiscordConfig().getChannelId())
                .data(RequestMidjourneyData.builder()
                        .options(Arrays.asList(RequestMidjourneyOptions.builder()
                                .value(prompt)
                                .build()))
                        .application_command(RequestMidjourneyApplicationCommand.builder()
                                .options(Arrays.asList(RequestMidjourneyOptionss.builder().build()))
                                .build())
                        .build())
                .build());

    }


    /**
     * Call ImageUpscale service
     * @param
     */
    public void upscale(int index, String messageId, String messageHash) {
         sendRequest( ReqUpscaleDiscord.builder()
                .guildId(midjourneyBotConfiguration.getDiscordConfig().getServerId())
                .channelId(midjourneyBotConfiguration.getDiscordConfig().getChannelId())
                .messageId(messageId)
                .data(new ReqUpscaleData(index,messageHash))
                .build());
    }

    /**
     * Call ImageVariation service
     * @param index
     * @param messageId
     * @param messageHash
     */
    @Override
    public void variate(int index, String messageId, String messageHash) {
        sendRequest(ReqVariationDiscord.builder()
                .guildId(midjourneyBotConfiguration.getDiscordConfig().getServerId())
                .channelId(midjourneyBotConfiguration.getDiscordConfig().getChannelId())
                .messageId(messageId)
                .data(new ReqVariationData(index,messageHash))
                .build()
        );
    }

    @Override
    public void maxUpscale(String messageId, String messageHash) {
        sendRequest(ReqMaxUpscaleDiscord.builder()
                .guildId(midjourneyBotConfiguration.getDiscordConfig().getServerId())
                .channelId(midjourneyBotConfiguration.getDiscordConfig().getChannelId())
                .messageId(messageId)
                .data(new ReqMaxUpscaleData(messageHash))
                .build());
    }

    @Override
    public void reSet(String messageId, String messageHash) {
        sendRequest(ReqReSetUpscaleDiscord.builder()
                .guildId(midjourneyBotConfiguration.getDiscordConfig().getServerId())
                .channelId(midjourneyBotConfiguration.getDiscordConfig().getChannelId())
                .messageId(messageId)
                .data(new ReqReSetUpscaleData(messageHash))
                .build());
    }




    /**
     * request DISCORD_API_URL
     * @param requestBody
     */
    private void sendRequest(Object requestBody) {
        webClient.post()
                .uri(DISCORD_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", midjourneyBotConfiguration.getDiscordConfig().getUserToken())
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> log.info("sendRequest response: " + response),
                        error -> log.error("Error: " + error.getMessage()));
    }
}
