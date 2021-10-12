package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Goods;
import com.snow.storeapi.entity.MyPage;
import com.snow.storeapi.mapper.GoodsMapper;
import com.snow.storeapi.service.IGoodsService;
import com.snow.storeapi.util.TransformCamelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Goods> implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Override
    @Transactional
    public MyPage findByDept(String sort, String deptId, String name, String preSku) {
//        var offset = (page - 1) * limit;
        if (sort != null && sort.startsWith("-")) {
            sort = sort.substring(1) + "DESC";
        }
        List<Goods> goodss = goodsMapper.findByDept(sort, deptId, name, preSku);
        var myPage = new MyPage();
        myPage.setItems(goodss);
        var total = goodsMapper.count();
        myPage.setTotal(total);
        return myPage;
    }
}
