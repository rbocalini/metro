package com.statusmetro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${statusmetro.api.base-url}")
    private String apiBaseUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(apiBaseUrl)
                .build();
    }
}
