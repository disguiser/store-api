package com.snow.storeapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.storeapi.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Mapper
@Component(value = "orderMapper")
public interface OrderMapper extends BaseMapper<Order> {

    List<Map<String,Object>> findByPage(@Param("start")Integer start, @Param("end")Integer end, @Param("address")String address, @Param("startDate") LocalDateTime startDate, @Param("endDate")LocalDateTime endDate);

    List<Map<String,Object>> getDetailByOrderId(@Param("orderId")Integer orderId);

    List<Map<String,Object>> getOrderDataById(@Param(value = "orderId")Integer orderId);

    List<Map<String,Object>> getGroupBy(@Param(value = "orderId")Integer orderId);
}
