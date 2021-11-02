package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Order;
import com.snow.storeapi.entity.OrderGoods;
import com.snow.storeapi.entity.Stock;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.service.IOrderGoodsService;
import com.snow.storeapi.service.IOrderService;
import com.snow.storeapi.service.IStockService;
import com.snow.storeapi.util.JwtUtils;
import com.snow.storeapi.util.ResponseUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "category", required = true) Integer category,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestBody(required = false) Map timeMap
    ) {
        if (timeMap == null) {
            timeMap = new HashMap<String, Object>();
        }
        timeMap.put("page", page);
        timeMap.put("limit", limit);
        timeMap.put("category", category);
        if (!StrUtil.isEmpty(address)) {
            timeMap.put("address", address);
        }
        if (!StrUtil.isEmpty(customerName)) {
            timeMap.put("customerName", customerName);
        }
        return ResponseUtil.listRes(orderService.findByPage(timeMap));
    }

    @ApiOperation("根据订单id查询详情")
    @GetMapping("/getDetailByOrderId/{orderId}")
    public Map getDetailByOrderId(@PathVariable Integer orderId) {
        return ResponseUtil.listRes(orderService.getDetailByOrderId(orderId));
    }

    @ApiOperation("添加")
    @PostMapping("/create")
    @Transactional(rollbackFor = Exception.class)
    public int create(@Valid @RequestBody Order order, HttpServletRequest request) {
        User user = JwtUtils.getSub(request);
        order.setInputUser(user.getId());
        orderService.save(order);
        var orderGoodsList = new ArrayList<OrderGoods>();
        var stockList = new ArrayList<Stock>();
        for (Map<String, Object> map : order.getStockList()) {
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setStockId((Integer) map.get("stockId"));
            orderGoods.setOrderId(order.getId());
            orderGoods.setAmount((Integer) map.get("amount"));
            orderGoods.setSalePrice(new BigDecimal(String.valueOf(map.get("salePrice"))));
            orderGoods.setSubtotalMoney(new BigDecimal(String.valueOf(map.get("subtotalMoney"))));
            orderGoodsList.add(orderGoods);
            // 更新商品现有库存
            Stock stock = new Stock();
            stock.setId((Integer) map.get("stockId"));
            stock.setCurrentStock((Integer) map.get("currentStock") - (Integer) map.get("amount"));
            stockList.add(stock);
        }
        stockService.updateBatchById(stockList);
        orderGoodsService.saveBatch(orderGoodsList);
        return order.getId();
    }

    @ApiOperation("修改")
    @PatchMapping("/update/{id}")
    @Transactional(rollbackFor = Exception.class)
    public int update(@PathVariable Integer id, @Valid @RequestBody Order order) {
        orderService.updateById(order);
        return order.getId();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/delete/{id}")
    @Transactional(rollbackFor = Exception.class)
    public void delete(@PathVariable Integer id) {
        //更新商品表的库存 & 删除明细表
        QueryWrapper<OrderGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", id);
        List<OrderGoods> list = orderGoodsService.list(wrapper);
        if (list != null && list.size() > 0) {
            var stockList = new ArrayList<Stock>();
            list.forEach(orderGoods -> {
                Stock current = stockService.getById(orderGoods.getStockId());
                if (current != null) {
                    Stock stock = new Stock();
                    stock.setId(orderGoods.getStockId());
                    stock.setCurrentStock(current.getCurrentStock() + orderGoods.getAmount());
                    stockList.add(stock);
                }
                stockService.updateBatchById(stockList);
            });
        }
        orderGoodsService.remove(wrapper);
        orderService.removeById(id);
    }

    @ApiOperation("出货单数据")
    @GetMapping("getOrderDataById/{id}")
    public ResponseEntity getOrderDataById(@PathVariable Integer id) {
        Order order = orderService.getById(id);
        if (order == null) {
            Map<String, Object> res = new HashMap<>();
            res.put("msg", "未查到下单信息！");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        return ResponseEntity.ok(orderService.getOrderDataById(id));
    }
}
