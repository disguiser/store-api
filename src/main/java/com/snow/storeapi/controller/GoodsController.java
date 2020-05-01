package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.Goods;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.entity.Vip;
import com.snow.storeapi.service.IGoodsService;
import com.snow.storeapi.service.IVipService;
import com.snow.storeapi.util.JwtUtils;
import com.snow.storeapi.util.ResponseUtil;
import com.snow.storeapi.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);
    
    @Autowired
    private IGoodsService goodsService;


    @ApiOperation("列表查询")
    @GetMapping("/findByPage")
    public Map list(
            @RequestParam(value = "name", required = false)String name,
            @RequestParam(value = "page", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit
    ) {
        IPage<Goods> page = new Page<>(pageNum, limit);
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        queryWrapper.eq("deleted",0);
        queryWrapper.orderByDesc("create_time");
        IPage<Goods> goodss = goodsService.page(page, queryWrapper);
        return ResponseUtil.pageRes(goodss);
    }

    @ApiOperation("添加")
    @PutMapping("/create")
    public int create(@Valid @RequestBody Goods goods) {
        goodsService.save(goods);
        return goods.getId();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/delete")
    public void delete(@RequestBody List<Integer> ids) {
        //goodsService.removeByIds(ids);
        //逻辑删除
        List<Goods> goodsList = new ArrayList<>(ids.size());
        ids.forEach(id->{
            Goods goods = new Goods();
            goods.setId(id);
            goods.setDeleted(1);
            goodsList.add(goods);
        });
        goodsService.updateBatchById(goodsList);
    }

    @ApiOperation("更新")
    @PatchMapping("/update/{id}")
    public int update(@PathVariable Integer id,@Valid @RequestBody Goods goods){
        goodsService.updateById(goods);
        return goods.getId();
    }

}
