package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.snow.storeapi.entity.Dict;
import com.snow.storeapi.security.HasRoles;
import com.snow.storeapi.service.IDictService;
import com.snow.storeapi.service.IVersionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
public class DictController {
    private final IDictService dictService;
    private final IVersionService versionService;
    @GetMapping("/all")
    public List<Dict> findAll(
            @RequestParam(value = "dictName", required = false)String dictName,
            @RequestParam(value = "sort", required = false) String sort
    ) {
        if (!StrUtil.isEmpty(dictName)) {
            dictName = null;
        }
//        if (StrUtil.isEmpty(sort)) {
//            sort = "-update_time";
//        } else {
//            sort = TransformCamelUtil.underline(sort);
//        }
        List<Dict> dicts = dictService.selectAll(dictName, sort);
        return dicts;
    }

    @PostMapping("")
    public int create(@Valid @RequestBody Dict dict) {
        versionService.addOne("dict");
        dictService.save(dict);
        return dict.getId();
    }

    @HasRoles({"Admin"})
    @PatchMapping("")
    public void update(@Valid @RequestBody Dict dict) {
        versionService.addOne("dict");
        dictService.updateById(dict);
    }

    @HasRoles({"Admin"})
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        dictService.removeById(id);
    }
}
