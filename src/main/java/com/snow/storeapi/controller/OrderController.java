package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.*;
import com.snow.storeapi.service.*;
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
import java.time.LocalDate;
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
    private IOrderGoodsService orderGoodsService;

    @Autowired
    private IStockService stockService;

    @ApiOperation("列表查询")
    @PostMapping("/findByPage")
    public Map list(
            @RequestParam(value = "page", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit,
            @RequestBody Map map
    ) {
        return ResponseUtil.listRes(orderService.findByPage(pageNum,limit,map));
    }

    @ApiOperation("根据订单id查询详情")
    @GetMapping("/getDetailByOrderId/{orderId}")
    public Map getDetailByOrderId(@PathVariable Integer orderId){
        return ResponseUtil.listRes(orderService.getDetailByOrderId(orderId));
    }

    @ApiOperation("添加")
    @PostMapping("/create")
    @Transactional(rollbackFor = Exception.class)
    public int create(@Valid @RequestBody Order order, User user) {
        order.setInputUser(user.getId());
        orderService.save(order);
        for(Map<String, Object> map : order.getStockList()){
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setStockId((Integer) map.get("stockId"));
            orderGoods.setOrderId(order.getId());
            orderGoods.setAmount(new BigDecimal((Integer) map.get("amount")));
            orderGoods.setSubtotalMoney(new BigDecimal((Integer) map.get("subtotalMoney")));
            orderGoodsService.save(orderGoods);
            //更新商品现有库存
            Stock stock = new Stock();
            stock.setId((Integer) map.get("stockId"));
            stock.setCurrentStock(new BigDecimal((Integer) map.get("currentStock")).subtract(new BigDecimal((Integer) map.get("amount"))));
            stockService.updateById(stock);
        }
        return order.getId();
    }

    @ApiOperation("批量删除/作废")
    @DeleteMapping("/delete/{id}")
    @Transactional(rollbackFor = Exception.class)
    public void delete(@PathVariable Integer id) {
        //更新商品表的库存 & 删除明细表
        QueryWrapper<OrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",id);
        List<OrderGoods> list = orderGoodsService.list(wrapper);
        if(list != null && list.size() > 0){
            list.forEach(orderGoods -> {
                Stock current = stockService.getById(orderGoods.getStockId());
                if(current != null) {
                    Stock stock = new Stock();
                    stock.setId(orderGoods.getStockId());
                    stock.setCurrentStock(current.getCurrentStock().add(orderGoods.getAmount()));
                    stockService.updateById(stock);
                }
            });
        }
        orderGoodsService.remove(wrapper);
        orderService.removeById(id);
    }

    @ApiOperation("出货单数据")
    @GetMapping("getOrderDataById/{id}")
    public ResponseEntity getOrderDataById(@PathVariable Integer id){
        Order order = orderService.getById(id);
        if(order == null){
            Map<String, Object> res = new HashMap<>();
            res.put("msg","未查到下单信息！");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        return ResponseEntity.ok(orderService.getOrderDataById(id));
    }
}
