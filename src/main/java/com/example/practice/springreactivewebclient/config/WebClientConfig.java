package com.example.practice.springreactivewebclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig implements WebClientCustomizer {
    private final String rootUrl;
    private final ReactiveOAuth2AuthorizedClientManager clientManager;

    public WebClientConfig(@Value("${webclient.rootUrl}") String rootUrl, ReactiveOAuth2AuthorizedClientManager clientManager) {
        this.rootUrl = rootUrl;
        this.clientManager = clientManager;
    }


    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction filterFunction = new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientManager);
        filterFunction.setDefaultClientRegistrationId("springauth");
        webClientBuilder.filter(filterFunction).baseUrl(rootUrl);
    }
}
