package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.Goods;
import com.snow.storeapi.entity.MyPage;
import com.snow.storeapi.entity.Stock;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.service.IGoodsService;
import com.snow.storeapi.service.IStockService;
import com.snow.storeapi.service.IVersionService;
import com.snow.storeapi.util.ResponseUtil;
import com.snow.storeapi.util.TransformCamelUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * 商品
 */
@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
//    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);
    private final IGoodsService goodsService;
    private final IStockService stockService;
    private final IVersionService versionService;

    @ApiOperation("列表查询")
    @GetMapping("/page")
    public Map findByPage(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "preSku", required = false) String preSku,
            @RequestParam(value = "sort", defaultValue = "-modifyTime") String sort,
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
        return ResponseUtil.pageRes(goodss);
    }

    // 包含明细 sql手写
    @ApiOperation("列表查询")
    @GetMapping("/dept")
    public Map findByDept(
            @RequestParam(value = "deptId", required = false)String deptId,
            @RequestParam(value = "name", required = false)String name,
            @RequestParam(value = "preSku", required = false)String preSku,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page")Integer page,
            @RequestParam(value = "limit")Integer limit
    ) {
        MyPage goodss = goodsService.findByDept(sort, deptId, name, preSku, page, limit);
        return ResponseUtil.pageRes(goodss);
    }

    @ApiOperation("添加")
    @PostMapping("")
    public Goods create(@Valid @RequestBody Goods goods) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        goods.setInputUser(user.getId());
        // generate sku
        var version = versionService.addOne("sku");
        goods.setSku(StrUtil.fillBefore(version.getV().toString(), '0', 4).toString());

        if(goods.getId() == null){
            goodsService.save(goods);
        }
//        goods.getStocks().forEach(stock -> {
//            stock.setGoodsId(goods.getId());
//            //对stock表操作
//            //根据颜色和尺码查询，库存表中是否存在
//            QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("color",stock.getColor());
//            queryWrapper.eq("size",stock.getSize());
//            queryWrapper.eq("goods_id",stock.getGoodsId());
//            Stock s = stockService.getOne(queryWrapper);
//            if (s == null){
//                //没有库存
//                stock.setCurrentStock(stock.getAmount());
//                stockService.save(stock);
//            }else {
//                stock.setId(s.getId());
//                //更新库存数量
//                s.setCurrentStock(s.getCurrentStock() + stock.getAmount());
//                stockService.updateById(s);
//            }
//            //对purchase表进行操作
//            Purchase purchase = new Purchase();
//            purchase.setStockId(stock.getId());
//            purchase.setPurchaseAmount(stock.getAmount());
//            purchaseService.save(purchase);
//        });
        return goods;
    }

    @ApiOperation("删除")
    @DeleteMapping({"/{goodsId}", "/{goodsId}/{deptId}"})
    @Transactional(rollbackFor = Exception.class)
    public void delete(
            @PathVariable Integer goodsId,
            @PathVariable(required = false) Optional<Integer> deptId
    ) {
        var queryWrapper = new QueryWrapper<Stock>().eq("goods_id", goodsId);
        if (deptId.isPresent()) {
            queryWrapper.eq("dept_id", deptId);
        }
        stockService.remove(queryWrapper);
        goodsService.removeById(goodsId);
    }

    @ApiOperation("更新")
    @PatchMapping("/{id}")
    public int update(@PathVariable Integer id,@Valid @RequestBody Goods goods){
        goodsService.updateById(goods);
        return goods.getId();
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity findOneBySku(@PathVariable String sku){
        if (sku == null || StrUtil.isEmpty(sku)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("sku不能为空！");
        }
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sku",sku.toUpperCase());
        return  ResponseEntity.ok(goodsService.getOne(queryWrapper));
    }
}
