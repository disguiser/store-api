package com.snow.storeapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.storeapi.entity.SizeGroup;

import java.util.List;

public interface ISizeGroupService extends IService<SizeGroup> {
    List<SizeGroup> findAll();
}
