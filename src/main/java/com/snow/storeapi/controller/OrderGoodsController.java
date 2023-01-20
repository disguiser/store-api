package com.snow.storeapi.controller;

import com.snow.storeapi.entity.Order;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.enums.Role;
import com.snow.storeapi.service.ICustomerService;
import com.snow.storeapi.service.IOrderGoodsService;
import com.snow.storeapi.service.IOrderService;
import com.snow.storeapi.util.JwtUtils;
import com.snow.storeapi.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 今日零售商品列表
 */
@RestController
@RequestMapping("/order-goods")
public class OrderGoodsController {

    @Autowired
    private IOrderGoodsService orderGoodsService;

    @ApiOperation("列表查询")
    @GetMapping("/daily-list")
    public List dailyList(
            @RequestParam(value = "sort", required = false) String sort
    ) {
        return orderGoodsService.dailyList(sort);
    }
}
