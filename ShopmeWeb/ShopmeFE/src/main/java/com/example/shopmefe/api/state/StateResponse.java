package com.example.shopmefe.api.state;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StateResponse(@JsonProperty("id") Integer stateId,
                            @JsonProperty("name") String name) {
}
