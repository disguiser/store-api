package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Stock;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.service.IStockService;
import com.snow.storeapi.util.BaseContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存
 */
@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {
    private final IStockService stockService;
    @GetMapping("/sum")
    public Integer sumByDept() {
        User user = BaseContext.getCurrentUser();
        return stockService.sumByDept(user.getDeptId());
    }

    @GetMapping("/list")
    public ResponseEntity list(
            @RequestParam(value = "goodsId", required = false) Integer goodsId,
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

    @PostMapping("/single")
    public Integer create(@Valid @RequestBody Stock stock) {
        stockService.save(stock);
        return stock.getId();
    }

    @PostMapping("/multi")
    public void multiCreate(@RequestBody Stock[] stocks) {
        for (var stock: stocks) {
            var _stock = stockService.getOne(
                    new QueryWrapper<Stock>()
                            .eq("dept_id", stock.getDeptId())
                            .eq("goods_id", stock.getGoodsId())
                            .eq("color", stock.getColor())
                            .eq("size", stock.getSize())
            );
            if (_stock != null) {
                _stock.setCurrentStock(_stock.getCurrentStock() + stock.getCurrentStock());
                stockService.updateById(_stock);
            } else {
                stockService.save(stock);
            }
        }
    }

    @DeleteMapping("")
    public void delete(@RequestBody List<Integer> ids) {
        stockService.removeByIds(ids);
    }

    @PatchMapping("/{id}")
    public Integer update(@PathVariable Integer id,@Valid @RequestBody Stock stock){
        stockService.updateById(stock);
        return stock.getId();
    }

}
