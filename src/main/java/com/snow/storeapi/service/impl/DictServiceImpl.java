package com.snow.storeapi.service.impl;

import com.snow.storeapi.entity.Dict;
import com.snow.storeapi.mapper.DictMapper;
import com.snow.storeapi.service.IDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl implements IDictService {
    @Autowired
    private DictMapper dictMapper;
    @Override
    public Integer insert(Dict dict) {
        return dictMapper.insert(dict);
    }

    @Override
    public void deleteById(Integer id) {
        dictMapper.deleteById(id);
    }

    @Override
    public Integer updateById(Dict dict) {
        return dictMapper.updateById(dict);
    }

    @Override
    public List<Dict> selectAll(String dictName, String sort) {
        return dictMapper.selectAll(dictName, sort);
    }
}
