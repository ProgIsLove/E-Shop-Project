package com.example.shopmebe.setting.country;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CountryRequest(@JsonProperty("name")
                             @NotNull(message = "The field cannot be empty")
                             @Size(min = 3, max = 50, message = "The field must be between 3 and 50 characters long")
                             @Pattern(regexp = "^[A-Za-z]+$", message = "Only alphabetical characters are allowed")
                             String name,
                             @JsonProperty("code")
                             @NotNull(message = "The field cannot be empty")
                             @Size(min = 2, max = 3, message = "The field must be between 2 and 3 characters long")
                             @Pattern(regexp = "^[A-Za-z]+$", message = "Only alphabetical characters are allowed")
                             String code) {
}
