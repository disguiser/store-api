package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.snow.storeapi.entity.Dict;
import com.snow.storeapi.service.IDictService;
import com.snow.storeapi.service.IVersionService;
import com.snow.storeapi.util.TransformCamelUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping("/create")
    public int create(@Valid @RequestBody Dict dict) {
        versionService.addOne("dict");
        dictService.save(dict);
        return dict.getId();
    }

    @ApiOperation("修改字典")
    @PatchMapping("/update")
    public void update(@Valid @RequestBody Dict dict) {
        versionService.addOne("dict");
        dictService.updateById(dict);
    }


    @ApiOperation("删除字典")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        dictService.removeById(id);
    }
}
