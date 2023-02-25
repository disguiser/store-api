package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Version;
import com.snow.storeapi.service.IVersionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/version")
@RequiredArgsConstructor
public class VersionController {
    private final IVersionService versionService;
    @ApiOperation("版本查询")
    @GetMapping("/findAll")
    public List<Version> findAll() {
        var versions = versionService.list();
        return versions;
    }

    @ApiOperation("版本修改")
    @PatchMapping("/update/{name}")
    public void update(@PathVariable String name) {
        versionService.addOne(name);
    }
}
