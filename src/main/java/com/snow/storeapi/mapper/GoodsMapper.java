package com.snow.storeapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.storeapi.entity.Goods;
import com.snow.storeapi.entity.MyPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper extends BaseMapper<Goods> {
    List<Goods> findByDept(@Param("offset")Integer offset, @Param("limit")Integer limit, @Param("sort")String sort, @Param("deptId")String deptId, @Param("name")String name, @Param("sku")String sku);

    Integer count();
}
