package org.example.vpn_bot.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Configuration
public class YooKassaConfig {

    @Value("${shop.id}")
    private String shopId;

    @Value("${shop.key}")
    private String shopKey;

    @Bean
    @SneakyThrows
    public RestClient createRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.yookassa.ru/v3/payments")
                .defaultHeaders(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setBasicAuth(shopId, shopKey);
                    headers.add("Idempotence-Key", UUID.randomUUID().toString());
                }).build();
    }
}
