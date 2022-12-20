package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Dept;
import com.snow.storeapi.mapper.DeptMapper;
import com.snow.storeapi.service.IDeptService;
import org.springframework.stereotype.Service;

@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {
}
