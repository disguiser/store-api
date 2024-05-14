package com.snow.storeapi.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.OrderGoods;
import com.snow.storeapi.entity.ReturnGoods;
import com.snow.storeapi.mapper.OrderGoodsMapper;
import com.snow.storeapi.mapper.ReturnGoodsMapper;
import com.snow.storeapi.service.IOrderGoodsService;
import com.snow.storeapi.service.IReturnGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReturnGoodsServiceImpl extends ServiceImpl<ReturnGoodsMapper, ReturnGoods> implements IReturnGoodsService {
}
