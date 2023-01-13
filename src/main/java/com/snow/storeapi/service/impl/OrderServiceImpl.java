package com.snow.storeapi.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Order;
import com.snow.storeapi.entity.OrderGoods;
import com.snow.storeapi.entity.Stock;
import com.snow.storeapi.mapper.OrderMapper;
import com.snow.storeapi.service.IOrderGoodsService;
import com.snow.storeapi.service.IOrderService;
import com.snow.storeapi.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IStockService stockService;

    @Autowired
    private IOrderGoodsService orderGoodsService;

    @Override
    public Integer create(Order order) {
        save(order);
        var orderGoodsList = new ArrayList<OrderGoods>();
        var stockList = new ArrayList<Stock>();
        for (Map<String, Object> map : order.getItemList()) {
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setStockId((Integer) map.get("stockId"));
            orderGoods.setOrderId(order.getId());
            orderGoods.setAmount((Integer) map.get("amount"));
            orderGoods.setSalePrice((Integer) map.get("salePrice"));
            orderGoods.setSubtotalMoney((Integer) map.get("subtotalMoney"));
            orderGoodsList.add(orderGoods);
            // 更新商品现有库存
            Stock stock = new Stock();
            stock.setId((Integer) map.get("stockId"));
            stock.setCurrentStock((Integer) map.get("currentStock") - (Integer) map.get("amount"));
            stockList.add(stock);
        }
        stockService.updateBatchById(stockList);
        orderGoodsService.saveBatch(orderGoodsList);
        return order.getId();
    }

    @Override
    public List<Map<String, ?>> findByPage(Integer page, Integer limit, Integer category, String address, String customerName, Long startDate, Long endDate) {
        if (StrUtil.isEmpty(address)) {
            address = null;
        }
        if (StrUtil.isEmpty(customerName)) {
            customerName = null;
        }
        int start = (page - 1) * limit;
        int end = limit;
        LocalDateTime _startDate = null;
        LocalDateTime _endDate = null;
        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (startDate != null) {
            _startDate =LocalDateTime.ofEpochSecond(startDate/1000,0, ZoneOffset.ofHours(8));
        } else {
            _startDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        }
        if (endDate != null) {
            _endDate =LocalDateTime.ofEpochSecond(endDate/1000 + 24 * 60 * 60,0, ZoneOffset.ofHours(8));
        } else {
            _endDate = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        }
        return orderMapper.findByPage(start,end,address,_startDate,_endDate, category, customerName);
    }

    @Override
    public List<Map<String, Object>> getDetailByOrderId(Integer orderId) {
        return orderMapper.getDetailByOrderId(orderId);
    }

    @Override
    public void delete(Integer id) {
        //更新商品表的库存 & 删除明细表
        QueryWrapper<OrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", id);
        List<OrderGoods> list = orderGoodsService.list(wrapper);
        if (list != null && list.size() > 0) {
            var stockList = new ArrayList<Stock>();
            list.forEach(orderGoods -> {
                Stock current = stockService.getById(orderGoods.getStockId());
                if (current != null) {
                    Stock stock = new Stock();
                    stock.setId(orderGoods.getStockId());
                    stock.setCurrentStock(current.getCurrentStock() + orderGoods.getAmount());
                    stockList.add(stock);
                }
                stockService.updateBatchById(stockList);
            });
        }
        orderGoodsService.remove(wrapper);
        removeById(id);
    }

    @Override
    public Integer sumMoney() {
        return orderMapper.sumMoney();
    }

    @Override
    public Integer sumAmount(Integer category) {
        return orderMapper.sumAmount(category);
    }
}
