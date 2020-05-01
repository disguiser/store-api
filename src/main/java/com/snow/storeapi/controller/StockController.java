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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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


    @ApiOperation("列表查询")
    @GetMapping("/findByPage")
    public Map list(
            @RequestParam(value = "color", required = false)String color,
            @RequestParam(value = "page", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit
    ) {
        IPage<Stock> page = new Page<>(pageNum, limit);
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(color)) {
            queryWrapper.eq("color", color);
        }
        queryWrapper.eq("deleted",0);
        queryWrapper.orderByDesc("create_time");
        IPage<Stock> stocks = stockService.page(page, queryWrapper);
        return ResponseUtil.pageRes(stocks);
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
