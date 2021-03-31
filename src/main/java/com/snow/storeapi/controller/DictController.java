package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Dict;
import com.snow.storeapi.entity.Version;
import com.snow.storeapi.service.IDictService;
import com.snow.storeapi.service.IVersionService;
import com.snow.storeapi.util.StringUtil;
import com.snow.storeapi.util.TransformCamelUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {
    @Autowired
    private IDictService dictService;
    @Autowired
    private IVersionService versionService;

    @ApiOperation("字典查询")
    @GetMapping("/findAll")
    public List<Dict> findAll(
            @RequestParam(value = "dictName", required = false)String dictName,
            @RequestParam(value = "sort", required = false) String sort
    ) {
        if (!StringUtil.isEmpty(dictName)) {
            dictName = null;
        }
        if (StringUtil.isEmpty(sort)) {
            sort = "-modify-time";
        } else {
            sort = TransformCamelUtil.underline(sort);
        }
        List<Dict> dicts = dictService.selectAll(dictName, sort);
        return dicts;
    }

    @ApiOperation("添加字典")
    @PostMapping("/create")
    public int create(@Valid @RequestBody Dict dict) {
        var queryWrapper = new QueryWrapper<Version>();
        var version = versionService.getOne(queryWrapper);
        versionService.updateById(version);
        dictService.insert(dict);
        return dict.getId();
    }

    @ApiOperation("修改字典")
    @PatchMapping("/update")
    public void update(@Valid @RequestBody Dict dict) {
        var queryWrapper = new QueryWrapper<Version>();
        var version = versionService.getOne(queryWrapper.eq("name", "dict"));
        version.setV(version.getV() + 1);
        versionService.updateById(version);
        dictService.updateById(dict);
    }


    @ApiOperation("删除字典")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        dictService.deleteById(id);
    }
}
