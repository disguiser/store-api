package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Bill;
import com.snow.storeapi.service.IBillService;
import com.snow.storeapi.service.ICustomerService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {
    private final IBillService billService;
    private final ICustomerService customerService;
    @ApiOperation("全部查询")
    @GetMapping("/all")
    public List findAll(
            @RequestParam(value = "customerId", required = false)Integer customerId
    ) {
        QueryWrapper queryWrapper = new QueryWrapper<Bill>().orderByAsc("id");
        System.out.println(customerId);
        if (customerId > 0) {
            queryWrapper.like("customer_id", customerId);
        }
        List<Bill> bills =  billService.list(queryWrapper);
        return bills;
    }
    
    @PostMapping("")
    public int create(@Valid @RequestBody Bill bill) {
        billService.save(bill);
        var customer = customerService.getById(bill.getCustomerId());
        customer.setDebt(customer.getDebt() + bill.getAmount());
        customerService.updateById(customer);
        return bill.getId();
    }

    @PatchMapping("")
    @Transactional(rollbackFor = Exception.class)
    public void update(@Valid @RequestBody Bill bill) {
        var oldBill = billService.getById(bill.getId());
        if (!oldBill.getAmount().equals(bill.getAmount())) {
            var customer = customerService.getById(bill.getCustomerId());
            customer.setDebt(customer.getDebt() - oldBill.getAmount() + bill.getAmount());
            customerService.updateById(customer);
        }
        billService.updateById(bill);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        var bill = billService.getById(id);
        var customer = customerService.getById(bill.getCustomerId());
        customer.setDebt(customer.getDebt() - bill.getAmount());
        customerService.updateById(customer);
        billService.removeById(id);
    }
}
