package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Color;
import com.snow.storeapi.service.IColorService;
import com.snow.storeapi.service.IVersionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/color")
@RequiredArgsConstructor
public class ColorController {
    private final IColorService colorService;
    private final IVersionService versionService;
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
    
    @PostMapping("")
    public int create(@Valid @RequestBody Color color) {
        versionService.addOne("color");
        colorService.save(color);
        return color.getId();
    }

    @PatchMapping("")
    @Transactional(rollbackFor = Exception.class)
    public void update(@Valid @RequestBody Color color) {
        versionService.addOne("color");
        colorService.updateById(color);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        colorService.removeById(id);
    }
}
