package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.Goods;
import com.snow.storeapi.entity.Order;
import com.snow.storeapi.entity.OrderGoods;
import com.snow.storeapi.entity.R;
import com.snow.storeapi.service.IGoodsService;
import com.snow.storeapi.service.IOrderGoodsService;
import com.snow.storeapi.service.IOrderService;
import com.snow.storeapi.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * 商品
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IOrderGoodsService orderGoodsService;

    @ApiOperation("列表查询")
    @GetMapping("/findByPage")
    public Map list(
            @RequestParam(value = "page", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit
    ) {
        IPage<Order> page = new Page<>(pageNum, limit);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("order_time");
        IPage<Order> orders = orderService.page(page, queryWrapper);
        return ResponseUtil.pageRes(orders);
    }

    @ApiOperation("添加")
    @PutMapping("/create")
    @Transactional
    public int create(@Valid @RequestBody Order order) {
        orderService.save(order);
        for(Map<String,Integer> stock : order.getStockList()){
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setStockId(stock.get("stockId"));
            orderGoods.setOrderId(order.getId());
            orderGoods.setAmount(new BigDecimal(stock.get("amount")));
            orderGoodsService.save(orderGoods);
            //更新商品现有库存 todo
        }
        return order.getId();
    }

    @ApiOperation("批量删除/作废")
    @DeleteMapping("/delete")
    public void delete(@RequestParam(value = "ids") List<Integer> ids) {
        Collection<Order> orderCollection = orderService.listByIds(ids);
        //更新商品表的库存 todo
        orderCollection.forEach(order -> {

        });
        orderService.removeByIds(ids);
    }

    @ApiOperation("出货单数据")
    @GetMapping("getOrderDataById")
    public ResponseEntity getOrderDataById(@RequestParam(value = "id")Integer id){
        Order order = orderService.getById(id);
        if(order == null){
            Map<String, Object> res = new HashMap<>();
            //return R.error("未查到下单信息！");
            res.put("msg","未查到下单信息！");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        return ResponseEntity.ok(orderService.getOrderDataById(id));
    }
}
