package com.fv.movies.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Converter {
    private Converter() {
    }

    private static final Gson gson = new Gson();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertToString(Object obj) {
        if (obj == null) {
            return "null";
        }
        return gson.toJson(obj);

    }

    public static String convertToStringify(Object objMessage) {

        try {
            return objectMapper.writeValueAsString(objMessage);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }


}
