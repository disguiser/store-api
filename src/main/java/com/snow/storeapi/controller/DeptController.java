package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Dept;
import com.snow.storeapi.service.IDeptService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门controller
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    private static final Logger logger = LoggerFactory.getLogger(DeptController.class);

    @Autowired
    private IDeptService deptService;

    @ApiOperation("全部部门查询")
    @GetMapping("/find-all")
    public List list(
            @RequestParam(value = "name", required = false)String deptName
    ) {
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
