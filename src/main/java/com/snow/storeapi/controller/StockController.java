package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.Stock;
import com.snow.storeapi.service.IStockService;
import com.snow.storeapi.util.ResponseUtil;
import com.snow.storeapi.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品
 */
@RestController
@RequestMapping("/stock")
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);
    
    @Autowired
    private IStockService stockService;


    @ApiOperation("list查询")
    @GetMapping("/findAll/{goodsId}")
    public ResponseEntity list(@PathVariable Integer goodsId) {
        if (goodsId == null){
            Map<String, Object> res = new HashMap<>();
            res.put("msg","商品id不能为空！");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", goodsId);
        queryWrapper.eq("deleted",0);
        queryWrapper.orderByDesc("create_time");
        List<Stock> stocks = stockService.list(queryWrapper);
        return ResponseEntity.ok(stocks);
    }

    @ApiOperation("添加")
    @PutMapping("/create")
    public int create(@Valid @RequestBody Stock stock) {
        stockService.save(stock);
        return stock.getId();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/delete")
    public void delete(@RequestBody List<Integer> ids) {
        //stockService.removeByIds(ids);
        //逻辑删除
        List<Stock> stockList = new ArrayList<>(ids.size());
        ids.forEach(id->{
            Stock stock = new Stock();
            stock.setId(id);
            stock.setDeleted(1);
            stockList.add(stock);
        });
        stockService.updateBatchById(stockList);
    }

    @ApiOperation("更新")
    @PatchMapping("/update/{id}")
    public int update(@PathVariable Integer id,@Valid @RequestBody Stock stock){
        stockService.updateById(stock);
        return stock.getId();
    }

}
