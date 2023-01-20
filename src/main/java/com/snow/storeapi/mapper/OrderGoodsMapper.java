package com.snow.storeapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.storeapi.entity.OrderGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderGoodsMapper extends BaseMapper<OrderGoods> {
    List<Map<String, Object>> dailyList(@Param("sort") String sort, @Param("desc") Boolean desc);
}
