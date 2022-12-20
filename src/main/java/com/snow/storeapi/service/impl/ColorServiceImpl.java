package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Color;
import com.snow.storeapi.mapper.ColorMapper;
import com.snow.storeapi.service.IColorService;
import org.springframework.stereotype.Service;

@Service
public class ColorServiceImpl extends ServiceImpl<ColorMapper, Color> implements IColorService {
}
