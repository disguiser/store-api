package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.PrintTemplate;
import com.snow.storeapi.service.IPrintTemplateService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/print-template")
@RequiredArgsConstructor
public class PrintTemplateController {
    private final IPrintTemplateService printTemplateService;

    @ApiOperation("列表查询")
    @GetMapping("/all")
    public List<PrintTemplate> findAll() {
        QueryWrapper<PrintTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("modify_time");
        List<PrintTemplate> printTemplates = printTemplateService.list(queryWrapper);
        return printTemplates;
    }

    @ApiOperation("新增打印模板")
    @PostMapping("")
    public int create(@Valid @RequestBody PrintTemplate printTemplate) {
        printTemplateService.save(printTemplate);
        return printTemplate.getId();
    }

    @ApiOperation("修改打印模版")
    @PatchMapping("")
    public void update(@Valid @RequestBody PrintTemplate printTemplate) {
        printTemplateService.updateById(printTemplate);
    }

    @ApiOperation("删除打印模板")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        printTemplateService.removeById(id);
    }
}
