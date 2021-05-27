package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snow.storeapi.entity.Customer;
import com.snow.storeapi.service.ICustomerService;
import com.snow.storeapi.util.ResponseUtil;
import com.snow.storeapi.util.TransformCamelUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 客户controller
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private ICustomerService customerService;

    @ApiOperation("客户列表查询")
    @GetMapping("/findByPage")
    public Map list(
            @RequestParam(value = "searchText", required = false) String searchText,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
        IPage<Customer> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, limit);
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isEmpty(searchText)) {
            queryWrapper.like("name", searchText).or().like("address", searchText);
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

    @ApiOperation("添加客户")
    @PostMapping("/create")
    public int create(@Valid @RequestBody Customer customer) {
        customerService.save(customer);
        return customer.getId();
    }

    @ApiOperation("修改客户")
    @PatchMapping("/update/{id}")
    public int update(@PathVariable Integer id, @Valid @RequestBody Customer customer) {
        customer.setId(id);
        customerService.updateById(customer);
        return customer.getId();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/delete")
    public void delete(@RequestBody List<Integer> ids) {
        customerService.removeByIds(ids);
    }
}
