package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Order;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.service.ICustomerService;
import com.snow.storeapi.service.IOrderGoodsService;
import com.snow.storeapi.service.IOrderService;
import com.snow.storeapi.service.IStockService;
import com.snow.storeapi.util.JwtUtils;
import com.snow.storeapi.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICustomerService customerService;

    private Object HashMap;

    @ApiOperation("列表查询")
    @GetMapping("/findByPage")
    public Map list(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "category", required = true) Integer category,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "startDate", required = false) Long startDate,
            @RequestParam(value = "endDate", required = false) Long endDate
    ) {
        return ResponseUtil.listRes(orderService.findByPage(page, limit, category, address, customerName, startDate, endDate));
    }

    @ApiOperation("根据订单id查询详情")
    @GetMapping("/getDetailByOrderId/{orderId}")
    public Map mgetDetailByOrderId(@PathVariable Integer orderId) {
        return ResponseUtil.listRes(orderService.getDetailByOrderId(orderId));
    }

    @ApiOperation("添加")
    @PostMapping("")
    @Transactional(rollbackFor = Exception.class)
    public int create(@Valid @RequestBody Order order, HttpServletRequest request) {
        User user = JwtUtils.getSub(request);
        order.setInputUser(user.getId());
        var orderId = orderService.create(order);
        if (order.getBuyer() != null) {
            var customer = customerService.getById(order.getBuyer());
            customer.setDebt(customer.getDebt() + order.getTotalMoney());
            customerService.updateById(customer);
        }
        return orderId;
    }

//    @ApiOperation("修改")
//    @PatchMapping("/update/{id}")
//    @Transactional(rollbackFor = Exception.class)
//    public int update(@PathVariable Integer id, @Valid @RequestBody Order order) {
//        var customer = customerService.getById(order.getBuyer());
//        switch (order.getPaymentStatus()) {
//            case PaymentStatus.UNDONE:
//                customer.setDebt(customer.getDebt().add(order.getTotalMoney()));
//                break;
//            case PaymentStatus.DONE:
//                customer.setDebt(customer.getDebt().subtract(order.getTotalMoney()));
//                break;
//        }
//        customerService.updateById(customer);
//        orderService.updateById(order);
//        return order.getId();
//    }


    @ApiOperation("批量删除")
    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public void delete(@PathVariable Integer id) {
        orderService.delete(id);
        var order = orderService.getById(id);
        if (order.getBuyer() != null) {
            var customer = customerService.getById(order.getBuyer());
            customer.setDebt(customer.getDebt() - order.getTotalMoney());
            customerService.updateById(customer);
        }
    }

    @GetMapping("/daily/money")
    public Integer dailyMoney(
    ) {
        return orderService.sumMoney();
    }

    @GetMapping("/daily/amount")
    public Integer dailyAmount(
            @RequestParam(value = "category") Integer category
    ) {
        return orderService.sumAmount(category);
    }

    @GetMapping("test")
    @ResponseBody()
    public Map test() {
        Map<String, Object> map = new HashMap<>();
        map.put("LocalDateTime", LocalDateTime.now());
        Date date = new Date();
        System.out.println(date.toString());
        map.put("date", date);
//        return ResponseEntity.ok(JSON.toJSONString(map));
        return map;
    }
}
