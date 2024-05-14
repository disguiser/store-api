package com.snow.storeapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.storeapi.entity.Order;
import com.snow.storeapi.entity.PageResponse;
import com.snow.storeapi.entity.Return;

import java.util.List;
import java.util.Map;

public interface IReturnService extends IService<Return> {
    Integer create(Return _return);

    PageResponse findByPage(Integer page, Integer limit, String address, String customerName, Long startDate, Long endDate);

    List<Map<String,Object>> getDetailByReturnId(Integer returnId);

    void delete(Integer id);
}
