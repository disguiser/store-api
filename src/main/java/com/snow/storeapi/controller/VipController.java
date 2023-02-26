package com.snow.storeapi.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.entity.Vip;
import com.snow.storeapi.service.IVipService;
import com.snow.storeapi.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhou
 * @since 2018-10-19
 */
@RestController
@RequestMapping("/vip")
@RequiredArgsConstructor
public class VipController {
    private final IVipService vipService;
    @ApiOperation("会员列表查询")
    @GetMapping("/page")
    public Map list(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            HttpServletRequest request
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        IPage<Vip> page = new Page<>(pageNum, limit);
        QueryWrapper<Vip> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        if (!StrUtil.isEmpty(phone)) {
            queryWrapper.eq("phone", phone);
        }
        queryWrapper.orderByDesc(true, "create_time");
        //不是老板,只能查自己门店下的
        /*if(!"".equals(user.getRoles())) {
            queryWrapper.eq("dept_id", user.getDeptId());
        }*/
        queryWrapper.orderByDesc("modify_time");
        IPage<Vip> vips = vipService.page(page, queryWrapper);
        return ResponseUtil.pageRes(vips);
    }

    @ApiOperation("会员列表查询_无分页")
    @GetMapping("/listNoPage")
    public Map listNoPage(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            HttpServletRequest request
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QueryWrapper<Vip> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        if (!StrUtil.isEmpty(phone)) {
            queryWrapper.eq("phone", phone);
        }
        //不是老板,只能查自己门店下的
        /*if(!"".equals(user.getRole())) {
            queryWrapper.eq("dept_id", user.getDeptId());
        }*/
        queryWrapper.orderByDesc("modify_time");
        List<Vip> vips = vipService.list(queryWrapper);
        return ResponseUtil.listRes(vips);
    }

    @ApiOperation("添加会员")
    @PutMapping("")
    public int create(@Valid @RequestBody Vip vip, HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        vip.setDeptId(user.getDeptId());
        vipService.save(vip);
        return vip.getId();
    }

    @ApiOperation("修改会员")
    @PatchMapping("/{id}")
    public void update(@Valid @RequestBody Vip vip) {
        vip.setModifyTime(LocalDateTime.now());
        vipService.updateById(vip);
    }


    @ApiOperation("批量删除会员")
    @DeleteMapping("")
    public void delete(@RequestBody List<Integer> ids) {
        vipService.removeByIds(ids);
    }


}
