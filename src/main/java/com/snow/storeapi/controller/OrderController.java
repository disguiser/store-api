package com.snow.storeapi.controller;

import com.snow.storeapi.entity.Order;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.service.ICustomerService;
import com.snow.storeapi.service.IOrderService;
import com.snow.storeapi.util.BaseContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final ICustomerService customerService;
    @GetMapping("/page")
    public ResponseEntity<?> list(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "category") Integer category,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "startDate", required = false) Long startDate,
            @RequestParam(value = "endDate", required = false) Long endDate
    ) {
        return ResponseEntity.ok(
            orderService.findByPage(page, limit, category, address, customerName, startDate, endDate)
        );
    }

    @GetMapping("/details/{orderId}")
    public ResponseEntity<?> getDetailByOrderId(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.getDetailByOrderId(orderId));
    }

    @PostMapping("")
    @Transactional(rollbackFor = Exception.class)
    public int create(@Valid @RequestBody Order order) {
        var orderId = orderService.create(order);
        if (order.getBuyer() != null) {
            var customer = customerService.getById(order.getBuyer());
            customer.setDebt(customer.getDebt() + order.getTotalMoney());
            customerService.updateById(customer);
        }
        return orderId;
    }

    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public void delete(@PathVariable Integer id) {
        var order = orderService.getById(id);
        if (order.getBuyer() != null) {
            var customer = customerService.getById(order.getBuyer());
            customer.setDebt(customer.getDebt() - order.getTotalMoney());
            customerService.updateById(customer);
        }
        orderService.delete(id);
    }

    @GetMapping("/daily/money")
    public Integer dailyMoney() {
        User user = BaseContext.getCurrentUser();
        if (null != user.getDeptId()) {
            return orderService.sumMoney(user.getDeptId());
        } else {
            return null;
        }
    }

    @GetMapping("/daily/amount")
    public Integer dailyAmount(@RequestParam(value = "category") Integer category) {
        User user = BaseContext.getCurrentUser();
        if (null != user.getDeptId()) {
            return orderService.sumAmount(category, user.getDeptId());
        } else {
            return null;
        }
    }

    @GetMapping("/chart/money")
    public List<Map<String, Object>> chartMoney(@RequestParam(value = "category") Integer category) {
        User user = BaseContext.getCurrentUser();
//        if (user.getRoles().contains(Role.BOSS)) {
//
//        }
        if (null != user.getDeptId()) {
            return orderService.chartMoney(user.getDeptId(), category);
        } else {
            return null;
        }
    }

    @GetMapping("/chart/amount")
    public List<Map<String, Object>> chartAmount(@RequestParam(value = "category") Integer category) {
        User user = BaseContext.getCurrentUser();
        if (null != user.getDeptId()) {
            return orderService.chartAmount(category, user.getDeptId());
        } else {
            return null;
        }
    }
}
