package com.snow.storeapi.controller;

import com.snow.storeapi.entity.PrintTemplate;
import com.snow.storeapi.service.IPrintTemplateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/print-template")
public class PrintTemplateController {
    @Autowired
    private IPrintTemplateService printTemplateService;

    @ApiOperation("列表查询")
    @GetMapping("/find-all")
    public List<PrintTemplate> findAll() {
        List<PrintTemplate> printTemplates = printTemplateService.list();
        return printTemplates;
    }

    @ApiOperation("新增打印模板")
    @PostMapping("/create")
    public int create(@Valid @RequestBody PrintTemplate printTemplate) {
        printTemplateService.save(printTemplate);
        return printTemplate.getId();
    }

    @ApiOperation("修改打印模版")
    @PatchMapping("/update")
    public void update(@Valid @RequestBody PrintTemplate printTemplate) {
        printTemplateService.updateById(printTemplate);
    }

    @ApiOperation("删除打印模板")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        printTemplateService.removeById(id);
    }
}
