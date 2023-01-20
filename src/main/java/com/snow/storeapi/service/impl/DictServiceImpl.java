package com.snow.storeapi.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Dict;
import com.snow.storeapi.mapper.DictMapper;
import com.snow.storeapi.service.IDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {
    @Autowired
    private DictMapper dictMapper;
//    @Override
//    public Integer insert(Dict dict) {
//        return dictMapper.insert(dict);
//    }
//
//    @Override
//    public void deleteById(Integer id) {
//        dictMapper.deleteById(id);
//    }

    @Override
    public Integer modifyById(Dict dict) {
        return dictMapper.updateById(dict);
    }

    @Override
    public List<Dict> selectAll(String dictName, String sort) {
        var desc = false;
        if (!StrUtil.isEmpty(sort)) {
            if (sort.startsWith("-")) {
                desc = true;
                sort = sort.substring(1);
            }
        }
        return dictMapper.selectAll(dictName, sort, desc);
    }
}
