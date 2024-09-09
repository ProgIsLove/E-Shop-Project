package com.example.shopmebe.fileutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Country;

public class MapperUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String deserializeCountries(String json) throws JsonProcessingException {
        var countries = objectMapper.readValue(json, new TypeReference<>() {});
        return objectMapper.writeValueAsString(countries);
    }

    public static Country deserializeCountry(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Country.class);
    }
}
