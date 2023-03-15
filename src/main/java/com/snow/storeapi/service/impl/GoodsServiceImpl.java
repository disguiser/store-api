package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Goods;
import com.snow.storeapi.entity.PageResponse;
import com.snow.storeapi.mapper.GoodsMapper;
import com.snow.storeapi.service.IGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Goods> implements IGoodsService {
    private final GoodsMapper goodsMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResponse findByDept(
            String sort,
            String deptId,
            String name,
            String preSku,
            Integer page,
            Integer limit
    ) {
        var offset = (page - 1) * limit;
        if (sort != null && sort.startsWith("-")) {
            sort = sort.substring(1) + "DESC";
        }
        List<Goods> goodss = goodsMapper.findByDept(sort, deptId, name, preSku, offset, limit);
        var total = goodsMapper.countByDept(deptId, name, preSku);
        return new PageResponse(total, goodss);
    }
}
