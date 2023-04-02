package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.Goods;
import com.snow.storeapi.entity.PageResponse;
import com.snow.storeapi.entity.Stock;
import com.snow.storeapi.service.IGoodsService;
import com.snow.storeapi.service.IStockService;
import com.snow.storeapi.util.TransformCamelUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品
 */
@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final IGoodsService goodsService;
    private final IStockService stockService;

    @GetMapping("/page")
    public ResponseEntity<?> findByPage(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "preSku", required = false) String preSku,
            @RequestParam(value = "sort", defaultValue = "-updateTime") String sort,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
        IPage<Goods> page = new Page<>(pageNum, limit);
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (!StrUtil.isEmpty(preSku)) {
            queryWrapper.like("pre_sku", preSku);
        }
        if (sort.startsWith("-")) {
            queryWrapper.orderByDesc(TransformCamelUtil.underline(sort.substring(1)));
        } else {
            queryWrapper.orderByAsc(TransformCamelUtil.underline(sort));
        }
        IPage<Goods> goodss = goodsService.page(page, queryWrapper);
        return ResponseEntity.ok(
                new PageResponse(goodss.getTotal(), goodss.getRecords())
        );
    }

    @GetMapping("/dept")
    public ResponseEntity<?> findByDept(
            @RequestParam(value = "deptId", required = false)String deptId,
            @RequestParam(value = "name", required = false)String name,
            @RequestParam(value = "preSku", required = false)String preSku,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page")Integer page,
            @RequestParam(value = "limit")Integer limit
    ) {
        PageResponse pageResponse = goodsService.findByDept(sort, deptId, name, preSku, page, limit);
        return ResponseEntity.ok(pageResponse);
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody Goods goods) {
        goodsService.save(goods);
        return ResponseEntity.ok(goods.getId());
    }

    @DeleteMapping({"/{goodsId}", "/{goodsId}/{deptId}"})
    @Transactional(rollbackFor = Exception.class)
    public void delete(
            @PathVariable Integer goodsId,
            @PathVariable(required = false) Integer deptId
    ) {
        var queryWrapper = new QueryWrapper<Stock>().eq("goods_id", goodsId);
        if (deptId > 0) {
            queryWrapper.eq("dept_id", deptId);
        }
        stockService.remove(queryWrapper);
        goodsService.removeById(goodsId);
    }

    @PatchMapping("/{id}")
    public int update(@Valid @RequestBody Goods goods){
        goodsService.updateById(goods);
        return goods.getId();
    }

    /**
     * 历史遗留 旧条码需要用到
     * @param sku 废弃遗留字段
     * @return goods
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<?> findOneBySku(@PathVariable String sku) {
        if (StrUtil.isEmpty(sku)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("sku不能为空！");
        }
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku",sku.toUpperCase());
        return  ResponseEntity.ok(goodsService.getOne(queryWrapper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable String id) {
        return ResponseEntity.ok(goodsService.getById(id));
    }

    @GetMapping("/check/pre-sku/{preSku}")
    public ResponseEntity<?> checkPreSku(@PathVariable String preSku) {
        List<Goods> goodsList = goodsService.list(Wrappers.lambdaQuery(Goods.class).eq(Goods::getPreSku, preSku));
        if (goodsList.size() > 0) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }
}
