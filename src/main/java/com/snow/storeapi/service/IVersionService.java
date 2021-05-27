package com.snow.storeapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.storeapi.entity.Version;

public interface IVersionService extends IService<Version> {
    Version addOne(String name);
}
