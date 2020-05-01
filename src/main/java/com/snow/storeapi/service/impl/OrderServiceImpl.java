package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.util.concurrent.AtomicDouble;
import com.snow.storeapi.entity.Order;
import com.snow.storeapi.entity.R;
import com.snow.storeapi.mapper.OrderMapper;
import com.snow.storeapi.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public Map getOrderDataById(Integer orderId) {
        List<Map<String, Object>> list = orderMapper.getOrderDataById(orderId);
        List<String> skuList = orderMapper.getDistinctSku(orderId);
        List<Map<String,Object>> after = new ArrayList<>();
        Map<String,Object> result = new HashMap<>();
        AtomicInteger total = new AtomicInteger();
        AtomicDouble totalMoney = new AtomicDouble();
        skuList.forEach(sku->{
            Map<String,Object> p = new HashMap();
            p.put("sku",sku);
            AtomicInteger sumTotal = new AtomicInteger();
            list.forEach(map->{
                if(sku.equals(map.get("sku"))){
                    p.putAll(map);
                    p.remove("size");
                    p.remove("amount");
                    p.put(map.get("size").toString(),map.get("amount"));
                    BigDecimal amount = new BigDecimal(map.get("amount").toString());
                    sumTotal.addAndGet(amount.intValue());
                }
            });
            p.put("sumTotal",sumTotal);
            BigDecimal sumTotalMoney = new BigDecimal(String.valueOf(sumTotal)).multiply(new BigDecimal(p.get("salePrice").toString()));
            p.put("sumTotalMoney",sumTotalMoney.toString());
            total.addAndGet(sumTotal.intValue());
            totalMoney.addAndGet(sumTotalMoney.doubleValue());
            after.add(p);
        });
        result.put("list",after);
        result.put("total",total);
        result.put("totalMoney",totalMoney);
        return R.ok(result);
    }
}
