package com.snow.storeapi.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    private static final Logger logger = LoggerFactory.getLogger(ResponseUtil.class);

    public static Map<String, Object> pageRes(IPage<?> page) {
        logger.debug("vip数据: " + page.getRecords());
        logger.debug("总条数 ------> " + page.getTotal());
        logger.debug("当前页数 ------> " + page.getCurrent());
        logger.debug("当前每页显示数 ------> " + page.getSize());
        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("items", page.getRecords());
        return map;
    }
}
