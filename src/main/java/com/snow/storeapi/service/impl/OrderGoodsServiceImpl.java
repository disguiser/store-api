package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.OrderGoods;
import com.snow.storeapi.mapper.OrderGoodsMapper;
import com.snow.storeapi.service.IOrderGoodsService;
import org.springframework.stereotype.Service;

@Service
public class OrderGoodsServiceImpl extends ServiceImpl<OrderGoodsMapper,OrderGoods> implements IOrderGoodsService {
}
