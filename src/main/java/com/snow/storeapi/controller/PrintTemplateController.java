package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.PrintTemplate;
import com.snow.storeapi.service.IPrintTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/print-template")
@RequiredArgsConstructor
public class PrintTemplateController {
    private final IPrintTemplateService printTemplateService;

    @GetMapping("/all")
    public List<PrintTemplate> findAll() {
        QueryWrapper<PrintTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("update_time");
        List<PrintTemplate> printTemplates = printTemplateService.list(queryWrapper);
        return printTemplates;
    }

    @PostMapping("")
    public int create(@Valid @RequestBody PrintTemplate printTemplate) {
        printTemplateService.save(printTemplate);
        return printTemplate.getId();
    }

    @PatchMapping("")
    public void update(@Valid @RequestBody PrintTemplate printTemplate) {
        printTemplateService.updateById(printTemplate);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        printTemplateService.removeById(id);
    }
}
