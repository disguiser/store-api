package com.snow.storeapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.storeapi.entity.Order;

import java.util.List;
import java.util.Map;

public interface IOrderService extends IService<Order> {
    Integer create(Order order);

    List<Map<String,?>> findByPage(Integer page, Integer limit, Integer category, String address, String customerName, Long startDate, Long endDate);

    List<Map<String,Object>> getDetailByOrderId(Integer orderId);

    void delete(Integer id);

    Integer sumMoney();

    Integer sumAmount(Integer category);
}
