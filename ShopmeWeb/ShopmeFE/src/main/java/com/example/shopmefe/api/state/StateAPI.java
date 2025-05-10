package com.example.shopmefe.api.state;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class StateAPI {

    private final WebClient client;

    public StateAPI(WebClient.Builder webClientBuilder) {
        this.client = webClientBuilder
                .baseUrl("http://localhost:8080/ShopmeAdmin/v1/states")
                .build();
    }

    public Mono<List<StateResponse>> statesByCountry(Integer countryId) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{countryId}")
                        .build(countryId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }
}
