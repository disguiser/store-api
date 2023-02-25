package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Dept;
import com.snow.storeapi.service.IDeptService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门controller
 */
@RestController
@RequestMapping("/dept")
@RequiredArgsConstructor
public class DeptController {
    private final IDeptService deptService;
    @ApiOperation("全部部门查询")
    @GetMapping("/all")
    public List list() {
        List<Dept> depts = deptService.list(new QueryWrapper<Dept>().orderByDesc("modify_time"));
        return depts;
    }

    @ApiOperation("添加部门")
    @PostMapping("")
    public int create(@Valid @RequestBody Dept dept) {
        deptService.save(dept);
        return dept.getId();
    }

    @ApiOperation("修改部门")
    @PatchMapping("")
    public void update(@Valid @RequestBody Dept dept) {
        deptService.updateById(dept);
    }


    @ApiOperation("删除部门")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        deptService.removeById(id);
    }
}
