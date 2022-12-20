package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Color;
import com.snow.storeapi.service.IColorService;
import com.snow.storeapi.service.IVersionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/color")
public class ColorController {
    @Autowired
    private IColorService colorService;
    @Autowired
    private IVersionService versionService;

    @ApiOperation("全部尺码查询")
    @GetMapping("/all")
    public List findAll(
            @RequestParam(value = "itemName", required = false)String itemName
    ) {
        QueryWrapper<Color> queryWrapper = new QueryWrapper<Color>().orderByAsc("id");
        if (!StrUtil.isEmpty(itemName)) {
            queryWrapper.like("item_name", itemName);
        }
        List<Color> colors =  colorService.list(queryWrapper);
        return colors;
    }
    
    @ApiOperation("添加颜色")
    @PostMapping("")
    public int create(@Valid @RequestBody Color color) {
        versionService.addOne("color");
        colorService.save(color);
        return color.getId();
    }

    @ApiOperation("修改颜色")
    @PatchMapping("")
    @Transactional
    public void update(@Valid @RequestBody Color color) {
        versionService.addOne("color");
        colorService.updateById(color);
    }


    @ApiOperation("删除颜色")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        colorService.removeById(id);
    }
}
