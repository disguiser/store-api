package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Size;
import com.snow.storeapi.service.ISizeService;
import com.snow.storeapi.service.IVersionService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/size")
@RequiredArgsConstructor
public class SizeController {
    private final ISizeService sizeService;
    private final IVersionService versionService;

    @ApiOperation("全部尺码查询")
    @GetMapping("/all")
    public List findAll(
            @RequestParam(value = "itemName", required = false)String itemName
    ) {
        QueryWrapper<Size> queryWrapper = new QueryWrapper<Size>().orderByAsc("id");
        if (!StrUtil.isEmpty(itemName)) {
            queryWrapper.like("item_name", itemName);
        }
        List<Size> sizes =  sizeService.list(queryWrapper);
        return sizes;
    }
    
    @ApiOperation("添加尺码")
    @PostMapping("")
    public int create(@Valid @RequestBody Size size) {
        versionService.addOne("size");
        sizeService.save(size);
        return size.getId();
    }

    @ApiOperation("修改尺码")
    @PatchMapping("")
    @Transactional(rollbackFor = Exception.class)
    public void update(@Valid @RequestBody Size size) {
        versionService.addOne("size");
        sizeService.updateById(size);
    }


    @ApiOperation("删除尺码")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        versionService.addOne("size");
        sizeService.removeById(id);
    }
}
