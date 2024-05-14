package com.snow.storeapi.controller;

import com.snow.storeapi.entity.Return;
import com.snow.storeapi.service.ICustomerService;
import com.snow.storeapi.service.IReturnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 退货
 */
@RestController
@RequestMapping("/return")
@RequiredArgsConstructor
public class ReturnController {
    private final IReturnService returnService;
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
                returnService.findByPage(page, limit, address, customerName, startDate, endDate)
        );
    }

    @GetMapping("/details/{orderId}")
    public ResponseEntity<?> getDetailByOrderId(@PathVariable Integer orderId) {
        return ResponseEntity.ok(returnService.getDetailByReturnId(orderId));
    }

    @PostMapping("")
    @Transactional(rollbackFor = Exception.class)
    public int create(@Valid @RequestBody Return _return) {
        var orderId = returnService.create(_return);
        return orderId;
    }

    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public void delete(@PathVariable Integer id) {
        returnService.delete(id);
    }

}
