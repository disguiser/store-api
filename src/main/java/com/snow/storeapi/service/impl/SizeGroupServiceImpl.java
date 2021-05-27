package com.snow.storeapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Dict;
import com.snow.storeapi.entity.DictItem;
import com.snow.storeapi.entity.SizeGroup;
import com.snow.storeapi.mapper.DictMapper;
import com.snow.storeapi.mapper.SizeGroupMapper;
import com.snow.storeapi.service.ISizeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SizeGroupServiceImpl extends ServiceImpl<SizeGroupMapper, SizeGroup> implements ISizeGroupService {
    @Autowired
    private SizeGroupMapper sizeGroupMapper;
    @Autowired
    private DictMapper dictMapper;
    @Override
    public List<SizeGroup> findAll() {
        var sizeGroups = sizeGroupMapper.selectList(null);
        var dict = dictMapper.selectOne(new QueryWrapper<Dict>().eq("dict_name", "尺码"));
        var sizeObj = new HashMap<Integer, DictItem>();
        for (DictItem e: dict.getData()) {
            sizeObj.put(e.getId(), e);
        }
        List<DictItem> data;
        for (SizeGroup sizeGroup : sizeGroups) {
            data = new ArrayList<>();
            for (Object id: sizeGroup.getData()) {
                data.add(sizeObj.get(id));
            }
            sizeGroup.setData(data);
        }
        return sizeGroups;
    }
}
