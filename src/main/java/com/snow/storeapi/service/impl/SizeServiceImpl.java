package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Size;
import com.snow.storeapi.mapper.SizeMapper;
import com.snow.storeapi.service.ISizeService;
import org.springframework.stereotype.Service;

@Service
public class SizeServiceImpl extends ServiceImpl<SizeMapper, Size> implements ISizeService {
}
