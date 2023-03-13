package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Dept;
import com.snow.storeapi.service.IDeptService;
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
    @GetMapping("/all")
    public List list() {
        List<Dept> depts = deptService.list(new QueryWrapper<Dept>().orderByDesc("update_time"));
        return depts;
    }

    @PostMapping("")
    public int create(@Valid @RequestBody Dept dept) {
        deptService.save(dept);
        return dept.getId();
    }

    @PatchMapping("")
    public void update(@Valid @RequestBody Dept dept) {
        deptService.updateById(dept);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        deptService.removeById(id);
    }
}
