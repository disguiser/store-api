package com.snow.storeapi.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.snow.storeapi.entity.DictItem;

public class MyUtils {
    public static Object parseJson(String jsonStr, Class<?> clazz) {
        if (null != jsonStr) {
            var json = JSON.parse(jsonStr);
            if (json instanceof JSONObject) {
                return JSON.parseObject(jsonStr, clazz);
            } else if (json instanceof JSONArray) {
                return JSONArray.parseArray(jsonStr, clazz);
            }
        }
        return null;
    }
}
