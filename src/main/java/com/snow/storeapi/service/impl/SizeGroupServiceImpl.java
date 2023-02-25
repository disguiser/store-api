package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Dict;
import com.snow.storeapi.entity.DictItem;
import com.snow.storeapi.entity.Size;
import com.snow.storeapi.entity.SizeGroup;
import com.snow.storeapi.mapper.DictMapper;
import com.snow.storeapi.mapper.SizeGroupMapper;
import com.snow.storeapi.mapper.SizeMapper;
import com.snow.storeapi.service.ISizeGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeGroupServiceImpl extends ServiceImpl<SizeGroupMapper, SizeGroup> implements ISizeGroupService {
    private final SizeGroupMapper sizeGroupMapper;
    private final SizeMapper sizeMapper;
    @Override
    public List<SizeGroup> findAll() {
        var sizeGroups = sizeGroupMapper.selectList(null);
        var sizes = sizeMapper.selectList(null);
        var sizeObj = new HashMap<Integer, Size>();
        for (Size size: sizes) {
            sizeObj.put(size.getId(), size);
        }
        List<Size> data;
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
