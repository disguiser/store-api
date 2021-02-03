package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snow.storeapi.entity.Dept;
import com.snow.storeapi.service.IDeptService;
import com.snow.storeapi.util.ResponseUtil;
import com.snow.storeapi.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Map;

/**
 * 部门controller
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    private static final Logger logger = LoggerFactory.getLogger(DeptController.class);

    @Autowired
    private IDeptService deptService;

    @ApiOperation("部门列表查询")
    @GetMapping("/findByPage")
    public Map list(
            @RequestParam(value = "name", required = false)String deptName,
            @RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit
    ) {
        IPage<Dept> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, limit);
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(deptName)) {
            queryWrapper.eq("name", deptName);
        }
        IPage<Dept> depts = deptService.page(page, queryWrapper);
        return ResponseUtil.pageRes(depts);
    }

    @ApiOperation("添加部门")
    @PostMapping("/create")
    public int create(@Valid @RequestBody Dept dept) {
        deptService.save(dept);
        return dept.getId();
    }

    @ApiOperation("修改部门")
    @PatchMapping("/update")
    public void update(@Valid @RequestBody Dept dept) {
        deptService.updateById(dept);
    }


    @ApiOperation("删除部门")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        deptService.removeById(id);
    }
}
