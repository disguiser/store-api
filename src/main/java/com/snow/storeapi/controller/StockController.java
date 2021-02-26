package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.Stock;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.service.IStockService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存
 */
@RestController
@RequestMapping("/stock")
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);
    
    @Autowired
    private IStockService stockService;


    @ApiOperation("list查询")
    @GetMapping("/find")
    public ResponseEntity list(
            @RequestParam(value = "goodId", required = false) Integer goodsId,
            @RequestParam(value = "deptId", required = false) Integer deptId
    ) {
//        if (goodsId == null){
//            Map<String, Object> res = new HashMap<>();
//            res.put("msg","商品id不能为空！");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
//        }
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        if (goodsId != null) {
            queryWrapper.eq("goods_id", goodsId);
        }
        if (deptId != null) {
            queryWrapper.eq("dept_id", deptId);
        }
        queryWrapper.orderByDesc("color");
        List<Stock> stocks = stockService.list(queryWrapper);
        return ResponseEntity.ok(stocks);
    }

    @ApiOperation("添加")
    @PutMapping("/create")
    public Integer create(@Valid @RequestBody Stock stock, HttpServletRequest request) {
        User user = JwtUtils.getSub(request);
        stock.setInputUser(user.getId());
        stockService.save(stock);
        return stock.getId();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/delete")
    public void delete(@RequestBody List<Integer> ids) {
        stockService.removeByIds(ids);
    }

    @ApiOperation("更新")
    @PatchMapping("/update/{id}")
    public Integer update(@PathVariable Integer id,@Valid @RequestBody Stock stock){
        stock.setModifyTime(LocalDateTime.now());
        stockService.updateById(stock);
        return stock.getId();
    }

}
