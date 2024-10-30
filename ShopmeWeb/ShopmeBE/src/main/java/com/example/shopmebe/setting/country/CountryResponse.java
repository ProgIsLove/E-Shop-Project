package com.example.shopmebe.setting.country;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CountryResponse(@JsonProperty("id") Integer id,
                              @JsonProperty("name") String name,
                              @JsonProperty("code") String code) {
}
