package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Version;
import com.snow.storeapi.mapper.VersionMapper;
import com.snow.storeapi.service.IVersionService;
import org.springframework.stereotype.Service;

@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements IVersionService {
    @Override
    public Version addOne(String name) {
        var version = getOne(new QueryWrapper<Version>().eq("name", name));
        version.setV(version.getV() + 1);
        updateById(version);
        return version;
    }
}
