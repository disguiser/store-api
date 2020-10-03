package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Version;
import com.snow.storeapi.mapper.VersionMapper;
import com.snow.storeapi.service.IVersionService;
import org.springframework.stereotype.Service;

@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements IVersionService {
}
