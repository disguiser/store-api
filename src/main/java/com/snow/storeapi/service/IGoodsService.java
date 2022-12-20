package com.snow.storeapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.storeapi.entity.Goods;
import com.snow.storeapi.entity.MyPage;

public interface IGoodsService extends IService<Goods> {
    MyPage findByDept(String sort, String deptId, String name, String preSku, Integer page, Integer limit);
}
