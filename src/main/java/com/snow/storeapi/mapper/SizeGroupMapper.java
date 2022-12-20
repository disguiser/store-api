package com.snow.storeapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.storeapi.entity.SizeGroup;

import java.util.List;
import java.util.Map;

public interface SizeGroupMapper extends BaseMapper<SizeGroup> {
    List<Map> selectAll();
}
