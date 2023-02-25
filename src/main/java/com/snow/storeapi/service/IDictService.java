package com.snow.storeapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.storeapi.entity.Dict;

import java.util.List;

public interface IDictService extends IService<Dict> {
    Integer modifyById(Dict dict);
    List<Dict> selectAll(String dictName, String sort);
}
