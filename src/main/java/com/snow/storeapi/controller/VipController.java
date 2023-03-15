package com.snow.storeapi.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.PageResponse;
import com.snow.storeapi.entity.Vip;
import com.snow.storeapi.service.IVipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/page")
    public ResponseEntity<?> list(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
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
        queryWrapper.orderByDesc("update_time");
        IPage<Vip> vips = vipService.page(page, queryWrapper);
        return ResponseEntity.ok(
                new PageResponse(vips.getTotal(), vips.getRecords())
        );
    }

    @GetMapping("/listNoPage")
    public ResponseEntity<?> listNoPage(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone
    ) {
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
        queryWrapper.orderByDesc("update_time");
        List<Vip> vips = vipService.list(queryWrapper);
        return ResponseEntity.ok(vips);
    }

    @PutMapping("")
    public int create(@Valid @RequestBody Vip vip) {
        vipService.save(vip);
        return vip.getId();
    }

    @PatchMapping("/{id}")
    public void update(@Valid @RequestBody Vip vip) {
        vipService.updateById(vip);
    }


    @DeleteMapping("")
    public void delete(@RequestBody List<Integer> ids) {
        vipService.removeByIds(ids);
    }


}
