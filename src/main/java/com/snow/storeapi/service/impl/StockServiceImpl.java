package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Stock;
import com.snow.storeapi.mapper.StockMapper;
import com.snow.storeapi.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl extends ServiceImpl<StockMapper,Stock> implements IStockService {
    @Autowired
    private StockMapper stockMapper;

    @Override
    public Integer sumByDept(Integer deptId) {
        return stockMapper.sumByDept(deptId);
    }
}
