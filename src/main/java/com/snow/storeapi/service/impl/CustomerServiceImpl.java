package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Customer;
import com.snow.storeapi.mapper.CustomerMapper;
import com.snow.storeapi.service.ICustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper,Customer> implements ICustomerService {
}
