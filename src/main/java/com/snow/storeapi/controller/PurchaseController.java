package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.Goods;
import com.snow.storeapi.entity.Purchase;
import com.snow.storeapi.service.IGoodsService;
import com.snow.storeapi.service.IPurchaseService;
import com.snow.storeapi.service.IStockService;
import com.snow.storeapi.util.ResponseUtil;
import com.snow.storeapi.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品
 */
@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);
    
    @Autowired
    private IPurchaseService purchaseService;

    @Autowired
    private IStockService stockService;


    @ApiOperation("列表查询")
    @GetMapping("/findByPage")
    public Map list(
            @RequestParam(value = "page", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit
    ) {
        IPage<Purchase> page = new Page<>(pageNum, limit);
        QueryWrapper<Purchase> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        IPage<Purchase> purchases = purchaseService.page(page, queryWrapper);
        return ResponseUtil.pageRes(purchases);
    }

    @ApiOperation("添加")
    @PutMapping("/create")
    public int create(@Valid @RequestBody Purchase purchase) {
        purchaseService.save(purchase);
        return purchase.getId();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/delete")
    public void delete(@RequestBody List<Integer> ids) {
        purchaseService.removeByIds(ids);
    }

}
