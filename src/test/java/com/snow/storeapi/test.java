package com.snow.storeapi;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONArray;
import com.snow.storeapi.entity.DictItem;
import com.xkzhangsan.time.calculator.DateTimeCalculatorUtil;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class test {
    public static void main(String[] args) {
//        byte[] encodedKey = Base64.decode(SystemConstant.JWT_SECERT);
//        byte[] a = SystemConstant.JWT_SECERT.getBytes();
//        System.out.println(encodedKey);
//        System.out.println(a);
//        var a = DateTimeCalculatorUtil.plusMinutes(LocalDateTime.now(), 20);
        Calendar gc =new GregorianCalendar();
        gc.setTime(new Date());
        gc.add(GregorianCalendar.MINUTE, 20);
        System.out.println(gc.getTime());

    }
}
