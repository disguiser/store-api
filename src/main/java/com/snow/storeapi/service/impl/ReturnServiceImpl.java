package com.snow.storeapi.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.DTO.returngoods.ReturnItemDTO;
import com.snow.storeapi.entity.PageResponse;
import com.snow.storeapi.entity.Return;
import com.snow.storeapi.entity.ReturnGoods;
import com.snow.storeapi.entity.Stock;
import com.snow.storeapi.mapper.ReturnMapper;
import com.snow.storeapi.service.IReturnGoodsService;
import com.snow.storeapi.service.IReturnService;
import com.snow.storeapi.service.IStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReturnServiceImpl extends ServiceImpl<ReturnMapper, Return> implements IReturnService {
    private final ReturnMapper returnMapper;
    private final IStockService stockService;
    private final IReturnGoodsService returnGoodsService;

    @Override
    public Integer create(Return _return) {
        save(_return);
        var returnGoodsList = new ArrayList<ReturnGoods>();
        var stockList = new ArrayList<Stock>();
        for (ReturnItemDTO returnItemDTO : _return.getItemList()) {
            ReturnGoods returnGoods = new ReturnGoods();
            returnGoods.setStockId(returnItemDTO.getStockId());
            returnGoods.setOrderId(_return.getId());
            returnGoods.setAmount(returnItemDTO.getAmount());
            returnGoodsList.add(returnGoods);
            // 更新商品现有库存
            Stock stock = new Stock();
            stock.setId(returnItemDTO.getStockId());
            stock.setCurrentStock(returnItemDTO.getCurrentStock() - returnItemDTO.getAmount());
            stockList.add(stock);
        }
        stockService.updateBatchById(stockList);
        returnGoodsService.saveBatch(returnGoodsList);
        return _return.getId();
    }

    @Override
    public PageResponse findByPage(Integer page, Integer limit, String address, String customerName, Long startDate, Long endDate) {
        if (StrUtil.isEmpty(address)) {
            address = null;
        }
        if (StrUtil.isEmpty(customerName)) {
            customerName = null;
        }
        int start = (page - 1) * limit;
        int end = limit;
        LocalDateTime _startDate;
        LocalDateTime _endDate;
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
        var list = returnMapper.findByPage(start,end,address,_startDate,_endDate, customerName);
        var total = returnMapper.count(address,_startDate,_endDate, customerName);
        return new PageResponse(total, list);
    }

    @Override
    public List<Map<String, Object>> getDetailByReturnId(Integer orderId) {
        return returnMapper.getDetailByReturnId(orderId);
    }

    @Override
    public void delete(Integer id) {
        //更新商品表的库存 & 删除明细表
        QueryWrapper<ReturnGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", id);
        List<ReturnGoods> list = returnGoodsService.list(wrapper);
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
        returnGoodsService.remove(wrapper);
        removeById(id);
    }
}
