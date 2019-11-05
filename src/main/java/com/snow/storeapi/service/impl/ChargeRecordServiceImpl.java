package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.ChargeRecord;
import com.snow.storeapi.mapper.ChargeRecordMapper;
import com.snow.storeapi.service.IChargeRecordService;
import org.springframework.stereotype.Service;

@Service
public class ChargeRecordServiceImpl extends ServiceImpl<ChargeRecordMapper,ChargeRecord> implements IChargeRecordService {
}
