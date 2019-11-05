package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.ConsumeRecord;
import com.snow.storeapi.mapper.ConsumeRecordMapper;
import com.snow.storeapi.service.IConsumeRecordService;
import org.springframework.stereotype.Service;

@Service
public class ConsumeRecordServiceImpl  extends ServiceImpl<ConsumeRecordMapper,ConsumeRecord> implements IConsumeRecordService {
}
