package com.snow.storeapi.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.snow.storeapi.entity.DictItem;

public class MyUtils {
    public static Object parseJson(String jsonStr) {
        if (null != jsonStr) {
            var json = JSON.parse(jsonStr);
            if (json instanceof JSONObject) {
                return JSON.parseObject(jsonStr, DictItem.class);
            } else {
                return JSONArray.parseArray(jsonStr, DictItem.class);
            }
        }
        return null;
    }
}
