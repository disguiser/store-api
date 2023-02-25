package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.snow.storeapi.entity.Dict;
import com.snow.storeapi.service.IDictService;
import com.snow.storeapi.service.IVersionService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dict")
@RequiredArgsConstructor
public class DictController {
    private final IDictService dictService;
    private final IVersionService versionService;
    @ApiOperation("字典查询")
    @GetMapping("/all")
    public List<Dict> findAll(
            @RequestParam(value = "dictName", required = false)String dictName,
            @RequestParam(value = "sort", required = false) String sort
    ) {
        if (!StrUtil.isEmpty(dictName)) {
            dictName = null;
        }
//        if (StrUtil.isEmpty(sort)) {
//            sort = "-modify_time";
//        } else {
//            sort = TransformCamelUtil.underline(sort);
//        }
        List<Dict> dicts = dictService.selectAll(dictName, sort);
        return dicts;
    }

    @ApiOperation("添加字典")
    @PostMapping("")
    public int create(@Valid @RequestBody Dict dict) {
        versionService.addOne("dict");
        dictService.save(dict);
        return dict.getId();
    }

    @ApiOperation("修改字典")
    @PatchMapping("")
    public void update(@Valid @RequestBody Dict dict) {
        versionService.addOne("dict");
        dictService.updateById(dict);
    }


    @ApiOperation("删除字典")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        dictService.removeById(id);
    }
}
