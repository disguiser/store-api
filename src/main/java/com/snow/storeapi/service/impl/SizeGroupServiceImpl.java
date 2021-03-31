package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.SizeGroup;
import com.snow.storeapi.mapper.SizeGroupMapper;
import com.snow.storeapi.service.ISizeGroupService;
import org.springframework.stereotype.Service;

@Service
public class SizeGroupServiceImpl extends ServiceImpl<SizeGroupMapper, SizeGroup> implements ISizeGroupService {
}
