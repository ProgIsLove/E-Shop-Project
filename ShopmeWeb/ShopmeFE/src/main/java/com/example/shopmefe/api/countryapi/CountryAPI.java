package com.example.shopmefe.api.countryapi;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CountryAPI {

    private final WebClient client;

    public CountryAPI(WebClient.Builder webClientBuilder) {
        this.client = webClientBuilder
                .baseUrl("http://localhost:8080/ShopmeAdmin/v1/countries")
                .build();
    }

    public Mono<List<CountryResponse>> countries() {
        return client.get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}
