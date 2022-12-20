package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Bill;
import com.snow.storeapi.mapper.BillMapper;
import com.snow.storeapi.service.IBillService;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements IBillService {
}
