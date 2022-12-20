package com.snow.storeapi.service.impl;

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

import java.time.LocalDateTime;
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
        for (Map<String, Object> map : order.getStockList()) {
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
    public List<Map<String, ?>> findByPage(Map<String,Object> query) {
        int page = (int) query.get("page");
        int limit = (int) query.get("limit");
        int start = (page - 1) * limit;
        int end = limit;
        String address = null;
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        String customerName = null;
        var category = 1;
        if (query.containsKey("category")) {
            category = (int) query.get("category");
        }
        if (query.containsKey("address")) {
            address = query.get("address").toString();
        }
        if (query.containsKey("customerName")) {
            customerName = query.get("customerName").toString();
        }
        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (query.containsKey("startDate")) {
            startDate =LocalDateTime.ofEpochSecond(Long.valueOf(query.get("startDate").toString())/1000,0, ZoneOffset.ofHours(8));
        }
        if (query.containsKey("endDate")) {
            endDate =LocalDateTime.ofEpochSecond(Long.valueOf(query.get("endDate").toString())/1000 + 24 * 60 * 60,0, ZoneOffset.ofHours(8));
        }
        return orderMapper.findByPage(start,end,address,startDate,endDate, category, customerName);
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
}
