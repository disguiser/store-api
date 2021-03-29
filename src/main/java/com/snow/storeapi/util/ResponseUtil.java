package com.snow.storeapi.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snow.storeapi.entity.MyPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseUtil {

    private static final Logger logger = LoggerFactory.getLogger(ResponseUtil.class);

    public static Map<String, Object> pageRes(IPage<?> page) {
        logger.debug("数据: " + page.getRecords());
        logger.debug("总条数 ------> " + page.getTotal());
        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("items", page.getRecords());
        return map;
    }

    public static Map<String, Object> pageRes(MyPage page) {
        logger.debug("数据: " + page.getItems());
        logger.debug("总条数 ------> " + page.getTotal());
        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("items", page.getItems());
        return map;
    }

    public static Map<String, Object> listRes(List<?> list) {
        logger.debug("数据: " + list);
        logger.debug("总条数 ------> " + list.size());
        Map<String, Object> map = new HashMap<>();
        map.put("total", list.size());
        map.put("items", list);
        return map;
    }

    public static Map stringRes(Object str) {
        var map = new HashMap<String, String>();
        map.put("result", str.toString());
        return map;
    }
}
