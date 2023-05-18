package com.pdfsolutions.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

public class CommonUtils {
    // I can remove this method
    public static <T> T JsonToObject(String json, Class<T> type) throws Exception, IOException {
        return new ObjectMapper().readValue(json, type);
    }
    
    public static JsonObject convertStringToJsonObj(String strObj) {
        return new Gson().fromJson(strObj, JsonObject.class);
    }
}
