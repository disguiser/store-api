package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.PrintTemplate;
import com.snow.storeapi.mapper.PrintTemplateMapper;
import com.snow.storeapi.service.IPrintTemplateService;
import org.springframework.stereotype.Service;

@Service
public class PrintTemplateServiceImpl extends ServiceImpl<PrintTemplateMapper, PrintTemplate> implements IPrintTemplateService {
}
