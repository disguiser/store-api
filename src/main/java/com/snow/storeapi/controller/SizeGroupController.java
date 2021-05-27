package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.SizeGroup;
import com.snow.storeapi.entity.Version;
import com.snow.storeapi.service.ISizeGroupService;
import com.snow.storeapi.service.IVersionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/size-group")
public class SizeGroupController {
    @Autowired
    private ISizeGroupService sizeGroupService;
    @Autowired
    private IVersionService versionService;

    @ApiOperation("列表查询")
    @GetMapping("/find-all")
    public List list() {
        List<SizeGroup> sizeGroups = sizeGroupService.findAll();
        return sizeGroups;
    }

    @ApiOperation("添加")
    @PostMapping("/create")
    @Transactional
    public SizeGroup create(@Valid @RequestBody SizeGroup sizeGroup) {
        sizeGroupService.save(sizeGroup);
        versionService.addOne("sizeGroup");
        return sizeGroup;
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete/{id}")
    @Transactional
    public void delete(@PathVariable Integer id) {
        versionService.addOne("sizeGroup");
        sizeGroupService.removeById(id);
    }

    @ApiOperation("更新")
    @PatchMapping("/update")
    @Transactional
    public void update(@Valid @RequestBody SizeGroup sizeGroup) {
        versionService.addOne("sizeGroup");
        sizeGroupService.updateById(sizeGroup);
    }
}
