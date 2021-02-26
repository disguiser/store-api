package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.GoodsCategory;
import com.snow.storeapi.entity.Version;
import com.snow.storeapi.service.IGoodsCategoryService;
import com.snow.storeapi.service.IVersionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/goods-category")
public class GoodsCategoryController {
    @Autowired
    private IGoodsCategoryService goodsCategoryService;
    @Autowired
    private IVersionService versionService;

    @ApiOperation("列表查询")
    @GetMapping("/find-all")
    public List list() {
        List<GoodsCategory> goodsCategories = goodsCategoryService.list();
        return goodsCategories;
    }

    @ApiOperation("添加")
    @PostMapping("/create")
    @Transactional
    public GoodsCategory create(@Valid @RequestBody GoodsCategory goodsCategory) {
        goodsCategoryService.save(goodsCategory);
        versionService.update(new QueryWrapper<Version>().eq("name", "goodsCategory"));
        return goodsCategory;
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete/{id}")
    @Transactional
    public void delete(@PathVariable Integer id) {
        versionService.update(new QueryWrapper<Version>().eq("name", "goodsCategory"));
        goodsCategoryService.removeById(id);
    }

    @ApiOperation("更新")
    @PatchMapping("/update")
    @Transactional
    public void update(@Valid @RequestBody GoodsCategory goodsCategory) {
        versionService.update(new QueryWrapper<Version>().eq("name", "goodsCategory"));
        goodsCategoryService.updateById(goodsCategory);
    }
}
