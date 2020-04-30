package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Goods;
import com.snow.storeapi.mapper.GoodsMapper;
import com.snow.storeapi.service.IGoodsService;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Goods> implements IGoodsService {
}
