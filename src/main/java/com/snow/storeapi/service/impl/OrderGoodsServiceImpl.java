package com.snow.storeapi.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.OrderGoods;
import com.snow.storeapi.mapper.OrderGoodsMapper;
import com.snow.storeapi.service.IOrderGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderGoodsServiceImpl extends ServiceImpl<OrderGoodsMapper,OrderGoods> implements IOrderGoodsService {
    private final OrderGoodsMapper orderGoodsMapper;
    @Override
    public List<Map<String, Object>> dailyList(String sort) {
        var desc = false;
        if (!StrUtil.isEmpty(sort)) {
            if (sort.startsWith("-")) {
                desc = true;
                sort = sort.substring(1);
            }
        }
        return orderGoodsMapper.dailyList(sort, desc);
    }
}
