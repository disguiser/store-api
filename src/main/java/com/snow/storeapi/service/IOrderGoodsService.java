package com.snow.storeapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.storeapi.entity.OrderGoods;

import java.util.List;
import java.util.Map;

public interface IOrderGoodsService extends IService<OrderGoods> {
    List<Map<String, Object>> dailyList(String sort);
}
