package com.chtw.midjourney.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class ChatServiceImpl  {


    @Value("${chatgpt.token}")
    private String token;


    /**
     * The constant openUrl.
     */
    private static final String openUrl = "https://api.openai.com/v1/";

    /**
     * Http string.
     *
     * @param body the body
     * @return the string
     */
    public JSONObject build(final Object body, final String url) {

        WebClient.Builder webClientBuilder = WebClient.builder();

        System.out.println(body.toString());

        final WebClient webClient = webClientBuilder.clientConnector(
                        new ReactorClientHttpConnector(HttpClient.create()
                                .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
                                        .host("127.0.0.1")
                                        .port(7890)
                                )))
                .baseUrl(openUrl)
                .defaultHeader("Authorization", "Bearer " +token)
                .build();

        log.info("=================>WebClient start http:");

        try {
            return JSONObject.parseObject(
                    webClient
                            .post()
                            .uri(url)
                            .body(BodyInserters.fromValue(body))
                            .retrieve()
                            .bodyToMono(String.class)
                            .timeout(Duration.ofSeconds(60))
                            .onErrorMap(TimeoutException.class, e -> new Exception())
                            .block()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }


}
