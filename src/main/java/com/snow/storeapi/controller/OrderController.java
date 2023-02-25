package com.snow.storeapi.controller;

import com.snow.storeapi.entity.Order;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.enums.Role;
import com.snow.storeapi.service.ICustomerService;
import com.snow.storeapi.service.IOrderService;
import com.snow.storeapi.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @ApiOperation("列表查询")
    @GetMapping("/page")
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
    @GetMapping("/details/{orderId}")
    public Map getDetailByOrderId(@PathVariable Integer orderId) {
        return ResponseUtil.listRes(orderService.getDetailByOrderId(orderId));
    }

    @ApiOperation("添加")
    @PostMapping("")
    @Transactional(rollbackFor = Exception.class)
    public int create(@Valid @RequestBody Order order, HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        order.setInputUser(user.getId());
        order.setDeptId(user.getDeptId());
        var orderId = orderService.create(order);
        if (order.getBuyer() != null) {
            var customer = customerService.getById(order.getBuyer());
            customer.setDebt(customer.getDebt() + order.getTotalMoney());
            customerService.updateById(customer);
        }
        return orderId;
    }

    @ApiOperation("批量删除")
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
    public Integer dailyMoney(HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (null != user.getDeptId()) {
            return orderService.sumMoney(user.getDeptId());
        } else {
            return null;
        }
    }

    @GetMapping("/daily/amount")
    public Integer dailyAmount(
            @RequestParam(value = "category") Integer category,
            HttpServletRequest request
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (null != user.getDeptId()) {
            return orderService.sumAmount(category, user.getDeptId());
        } else {
            return null;
        }
    }

    @GetMapping("/chart/money")
    public List<Map<String, Object>> chartMoney(
            @RequestParam(value = "category") Integer category,
            HttpServletRequest request
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (user.getRoles().contains(Role.BOSS)) {

        }
        if (null != user.getDeptId()) {
            return orderService.chartMoney(user.getDeptId(), category);
        } else {
            return null;
        }
    }

    @GetMapping("/chart/amount")
    public List<Map<String, Object>> chartAmount(
            @RequestParam(value = "category") Integer category,
            HttpServletRequest request
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (null != user.getDeptId()) {
            return orderService.chartAmount(category, user.getDeptId());
        } else {
            return null;
        }
    }
}
