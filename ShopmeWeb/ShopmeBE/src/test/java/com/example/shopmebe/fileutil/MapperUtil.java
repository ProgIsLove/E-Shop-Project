package com.example.shopmebe.fileutil;

import com.example.shopmebe.exception.ApiError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Country;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MapperUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String deserializeCountries(String json) throws JsonProcessingException {
        var countries = objectMapper.readValue(json, new TypeReference<>() {});
        return objectMapper.writeValueAsString(countries);
    }

    public static Country deserializeCountry(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Country.class);
    }

    public static ApiError deserializeErrorMsg(String json) throws IOException {
        return objectMapper.readValue(json, ApiError.class);
    }
}
