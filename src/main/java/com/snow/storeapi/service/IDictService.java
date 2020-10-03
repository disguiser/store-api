package com.snow.storeapi.service;

import com.snow.storeapi.entity.Dict;

import java.util.List;

public interface IDictService {
    Integer insert(Dict dict);
    void deleteById(Integer id);
    Integer updateById(Dict dict);
    List<Dict> selectAll(String dictName, String sort);
}
