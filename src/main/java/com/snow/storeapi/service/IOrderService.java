package com.snow.storeapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.storeapi.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IOrderService extends IService<Order> {

    Map getOrderDataById(@Param(value = "orderId")Integer orderId);
}
