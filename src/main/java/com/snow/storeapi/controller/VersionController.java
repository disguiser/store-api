package com.snow.storeapi.controller;

import com.snow.storeapi.entity.Version;
import com.snow.storeapi.service.IVersionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/version")
@RequiredArgsConstructor
public class VersionController {
    private final IVersionService versionService;
    @ApiOperation("版本查询")
    @GetMapping("/all")
    public List<Version> findAll() {
        var versions = versionService.list();
        return versions;
    }
}
