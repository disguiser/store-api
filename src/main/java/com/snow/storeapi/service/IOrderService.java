package com.snow.storeapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.storeapi.entity.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IOrderService extends IService<Order> {
    List<Map<String,Object>> findByPage(Integer page,Integer limit,Map<String,Object> map);

    List<Map<String,Object>> getDetailByOrderId(Integer orderId);

    Map getOrderDataById(Integer orderId);
}
