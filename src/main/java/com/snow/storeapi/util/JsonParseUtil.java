package com.snow.storeapi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class JsonParseUtil {
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static Object parseJson(String jsonStr, Class<?> clazz) {
        if (null != jsonStr) {
            try {
                var jsonNode = objectMapper.readTree(jsonStr);
                if (jsonNode.isArray()) {
                    return objectMapper.readValue(jsonStr, List.class);
                } else {
                    return objectMapper.readValue(jsonStr, clazz);
                }
            } catch (JsonProcessingException e) {
                log.error("", e);
            }
        }
        return null;
    }
//    public static Object parseJson(String jsonStr, Class<?> clazz) {
//        if (null != jsonStr) {
//            var json = JSON.parse(jsonStr);
//            if (json instanceof JSONObject) {
//                return JSON.parseObject(jsonStr, clazz);
//            } else if (json instanceof JSONArray) {
//                return JSONArray.parseArray(jsonStr, clazz);
//            }
//        }
//        return null;
//    }
}
