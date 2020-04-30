package com.snow.storeapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.storeapi.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Mapper
@Component(value = "orderMapper")
public interface OrderMapper extends BaseMapper<Order> {

    List<Map<String,Object>> getOrderDataById(@Param(value = "orderId")Integer orderId);

    List<String> getDistinctSku(@Param(value = "orderId")Integer orderId);
}
