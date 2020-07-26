package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.*;
import com.snow.storeapi.service.IGoodsService;
import com.snow.storeapi.service.IPurchaseService;
import com.snow.storeapi.service.IStockService;
import com.snow.storeapi.service.IVipService;
import com.snow.storeapi.util.JwtUtils;
import com.snow.storeapi.util.ResponseUtil;
import com.snow.storeapi.util.StringUtil;
import com.snow.storeapi.util.TransformCamelUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private IPurchaseService purchaseService;

    @Autowired
    private IStockService stockService;


    @ApiOperation("列表查询")
    @GetMapping("/findByPage")
    public Map list(
            @RequestParam(value = "name", required = false)String name,
            @RequestParam(value = "sku", required = false)String sku,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit
    ) {
        IPage<Goods> page = new Page<>(pageNum, limit);
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (!StringUtil.isEmpty(sku)) {
            queryWrapper.like("sku", sku);
        }
        if (StringUtil.isEmpty(sort)) {
            queryWrapper.orderByAsc("name");
        } else {
            if (sort.startsWith("-")) {
                queryWrapper.orderByDesc(TransformCamelUtil.underline(sort.substring(1)));
            } else {
                queryWrapper.orderByAsc(TransformCamelUtil.underline(sort));
            }
        }
        queryWrapper.eq("deleted",0);
        queryWrapper.orderByDesc("create_time");
        IPage<Goods> goodss = goodsService.page(page, queryWrapper);
        return ResponseUtil.pageRes(goodss);
    }

    @ApiOperation("添加")
    @PostMapping("/create")
    @Transactional(rollbackFor = Exception.class)
    public int create(@Valid @RequestBody Goods goods) {
        if(goods.getId() == null){
            goodsService.save(goods);
        }
        goods.getStocks().forEach(stock -> {
            stock.setGoodsId(goods.getId());
            //对stock表操作
            //根据颜色和尺码查询，库存表中是否存在
            QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("color",stock.getColor());
            queryWrapper.eq("size",stock.getSize());
            queryWrapper.eq("goods_id",stock.getGoodsId());
            Stock s = stockService.getOne(queryWrapper);
            if (s == null){
                //没有库存
                stock.setCurrentStock(stock.getAmount());
                stockService.save(stock);
            }else {
                stock.setId(s.getId());
                //更新库存数量
                s.setCurrentStock(s.getCurrentStock().add(stock.getAmount()));
                stockService.updateById(s);
            }
            //对purchase表进行操作
            Purchase purchase = new Purchase();
            purchase.setStockId(stock.getId());
            purchase.setPurchaseAmount(stock.getAmount());
            purchaseService.save(purchase);
        });
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

    @GetMapping("/findOneBySku/{sku}")
    public ResponseEntity findOneBySku(@PathVariable String sku){
        if (sku == null || StringUtil.isEmpty(sku)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("sku不能为空！");
        }
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku",sku);
        return  ResponseEntity.ok(goodsService.getOne(queryWrapper));
    }

    @GetMapping("/findStockBySku/{sku}")
    public ResponseEntity findStockBySku(@PathVariable String sku){
        if (sku == null || StringUtil.isEmpty(sku)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("sku不能为空！");
        }
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku",sku);
        queryWrapper.eq("deleted",0);
        Goods goods = goodsService.getOne(queryWrapper);
        if (goods == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("找不到商品！");
        }
        QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
        stockQueryWrapper.eq("goods_id",goods.getId());
        stockQueryWrapper.eq("deleted",0);
        return ResponseEntity.ok(stockService.list(stockQueryWrapper));
    }

}
