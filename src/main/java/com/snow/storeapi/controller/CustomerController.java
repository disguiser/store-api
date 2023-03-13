package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.Customer;
import com.snow.storeapi.service.ICustomerService;
import com.snow.storeapi.util.ResponseUtil;
import com.snow.storeapi.util.TransformCamelUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 客户controller
 */
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final ICustomerService customerService;

    @GetMapping("/page")
    public Map findByPage(
            @RequestParam(value = "searchText", required = false) String searchText,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
        IPage<Customer> page = new Page<>(pageNum, limit);
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isEmpty(searchText)) {
            if (searchText.matches("\\d+")) {
                queryWrapper.like("mobile", searchText);
            } else {
                queryWrapper.like("name", searchText);
            }
        }
        if (StrUtil.isEmpty(sort)) {
            queryWrapper.orderByAsc("name");
        } else {
            if (sort.startsWith("-")) {
                queryWrapper.orderByDesc(TransformCamelUtil.underline(sort.substring(1)));
            } else {
                queryWrapper.orderByAsc(TransformCamelUtil.underline(sort));
            }
        }
        IPage<Customer> customers = customerService.page(page, queryWrapper);
        return ResponseUtil.pageRes(customers);
    }

    @PostMapping("")
    public int create(@Valid @RequestBody Customer customer) {
        customerService.save(customer);
        return customer.getId();
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable Integer id, @Valid @RequestBody Customer customer) {
        customer.setId(id);
        customerService.updateById(customer);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        customerService.removeById(id);
    }

    @GetMapping("/{id}")
    public Customer findOne(@PathVariable Integer id) {
        return customerService.getById(id);
    }
}
