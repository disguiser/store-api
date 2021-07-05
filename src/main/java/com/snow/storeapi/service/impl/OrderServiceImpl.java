package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.util.concurrent.AtomicDouble;
import com.snow.storeapi.entity.Order;
import com.snow.storeapi.mapper.OrderMapper;
import com.snow.storeapi.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<Map<String, Object>> findByPage(Integer page,Integer limit,Map<String,Object> map) {
        int start = (page - 1) * limit;
        int end = limit;
        String address = null;
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (map != null || map.size() > 0){
            if (map.containsKey("address") && map.get("address") != null) {
                address = map.get("address").toString();
            }
            DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (map.containsKey("startDate") && map.get("startDate") != null) {
                startDate =LocalDateTime.ofEpochSecond(Long.valueOf(map.get("startDate").toString())/1000,0, ZoneOffset.ofHours(8));
            }
            if (map.containsKey("endDate") && map.get("endDate") != null) {
                endDate =LocalDateTime.ofEpochSecond(Long.valueOf(map.get("endDate").toString())/1000,0, ZoneOffset.ofHours(8));
            }
        }
        return orderMapper.findByPage(start,end,address,startDate,endDate);
    }

    @Override
    public List<Map<String, Object>> getDetailByOrderId(Integer orderId) {
        return orderMapper.getDetailByOrderId(orderId);
    }

    @Override
    public Map getOrderDataById(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        List<Map<String, Object>> list = orderMapper.getOrderDataById(orderId);
        List<Map<String,Object>> groupByList = orderMapper.getGroupBy(orderId);
        List<Map<String,Object>> after = new ArrayList<>();
        Map<String,Object> result = new HashMap<>();
        AtomicInteger total = new AtomicInteger();
        AtomicDouble totalMoney = new AtomicDouble();
        //唯一值 sku+name+color
        groupByList.forEach(groupBy->{
            Map<String,Object> p = new HashMap();
            AtomicInteger subtotal = new AtomicInteger();
            AtomicDouble subtotalMoney = new AtomicDouble();
            list.forEach(map->{
                if(
                        groupBy.get("sku").equals(map.get("sku")) &&
                        groupBy.get("name").equals(map.get("name")) &&
                        groupBy.get("color").equals(map.get("color"))
                ){
                    p.putAll(map);
                    p.remove("size");
                    p.remove("amount");
                    p.put(map.get("size").toString(),map.get("amount"));
                    BigDecimal amount = new BigDecimal(map.get("amount").toString());
                    subtotal.addAndGet(amount.intValue());
                    subtotalMoney.addAndGet(new BigDecimal(map.get("sumtotalMoney").toString()).doubleValue());
                }
            });
            p.put("subtotal",subtotal);
            //BigDecimal sumTotalMoney = new BigDecimal(String.valueOf(subtotal)).multiply(new BigDecimal(p.get("salePrice").toString()));
            p.put("subtotalMoney",subtotalMoney);
            total.addAndGet(subtotal.intValue());
            totalMoney.addAndGet(subtotalMoney.doubleValue());
            after.add(p);
        });
        result.put("list",after);
        result.put("total",order.getTotal());
        result.put("totalMoney",order.getTotalMoney());
        return result;
    }
}
