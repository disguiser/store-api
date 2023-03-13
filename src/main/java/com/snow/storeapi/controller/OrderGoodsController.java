package com.snow.storeapi.controller;

import com.snow.storeapi.service.IOrderGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 今日零售商品列表
 */
@RestController
@RequestMapping("/order-goods")
@RequiredArgsConstructor
public class OrderGoodsController {
    private final IOrderGoodsService orderGoodsService;

    @GetMapping("/daily-list")
    public List dailyList(
            @RequestParam(value = "sort", required = false) String sort
    ) {
        return orderGoodsService.dailyList(sort);
    }
}
