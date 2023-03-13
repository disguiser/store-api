package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Size;
import com.snow.storeapi.service.ISizeService;
import com.snow.storeapi.service.IVersionService;
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
    
    @PostMapping("")
    public int create(@Valid @RequestBody Size size) {
        versionService.addOne("size");
        sizeService.save(size);
        return size.getId();
    }

    @PatchMapping("")
    @Transactional(rollbackFor = Exception.class)
    public void update(@Valid @RequestBody Size size) {
        versionService.addOne("size");
        sizeService.updateById(size);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        versionService.addOne("size");
        sizeService.removeById(id);
    }
}
