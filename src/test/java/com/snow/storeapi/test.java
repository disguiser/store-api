package com.snow.storeapi;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.snow.storeapi.entity.DictItem;
import com.xkzhangsan.time.calculator.DateTimeCalculatorUtil;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class test {
    public static void main(String[] args) {
        var str = "[{\"itemCode\":\"1\"}]";
        var json = JSON.parse(str);
        System.out.println(JSON.parseArray(str, DictItem.class));
//        System.out.println(json instanceof JSONObject);
//        System.out.println(json instanceof JSONArray);
    }
}
