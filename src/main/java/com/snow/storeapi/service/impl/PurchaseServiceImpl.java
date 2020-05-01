package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Purchase;
import com.snow.storeapi.mapper.PurchaseMapper;
import com.snow.storeapi.service.IPurchaseService;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServiceImpl extends ServiceImpl<PurchaseMapper,Purchase> implements IPurchaseService {
}
