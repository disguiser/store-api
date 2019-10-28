package com.snow.storeapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.entity.Vip;
import com.snow.storeapi.service.IVipService;
import com.snow.storeapi.util.JwtUtils;
import com.snow.storeapi.util.ResponseUtil;
import com.snow.storeapi.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhou
 * @since 2018-10-19
 */
@RestController
@RequestMapping("/vip")
public class VipController {

    private static final Logger logger = LoggerFactory.getLogger(VipController.class);

    @Autowired
    private IVipService vipService;

    @ApiOperation("会员列表查询")
    @GetMapping("/list")
    public Map list(
            @RequestParam(value = "name", required = false)String name,
            @RequestParam(value = "phone", required = false)String phone,
            @RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit,
            HttpServletRequest request
    ) {
        User user = JwtUtils.getSub(request);
        IPage<Vip> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, limit);
        QueryWrapper<Vip> queryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        if (!StringUtil.isEmpty(phone)) {
            queryWrapper.eq("phone", phone);
        }
        //不是老板,只能查自己门店下的
        /*if(!"".equals(user.getRole())) {
            queryWrapper.eq("dept_id", user.getDeptId());
        }*/
        IPage<Vip> vips = vipService.page(page, queryWrapper);
        return ResponseUtil.pageRes(vips);
    }

    @ApiOperation("会员列表查询_无分页")
    @GetMapping("/listNoPage")
    public Map listNoPage(
            @RequestParam(value = "name", required = false)String name,
            @RequestParam(value = "phone", required = false)String phone,
            HttpServletRequest request
    ) {
        User user = JwtUtils.getSub(request);
        QueryWrapper<Vip> queryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        if (!StringUtil.isEmpty(phone)) {
            queryWrapper.eq("phone", phone);
        }
        //不是老板,只能查自己门店下的
        /*if(!"".equals(user.getRole())) {
            queryWrapper.eq("dept_id", user.getDeptId());
        }*/
        List<Vip> vips = vipService.list(queryWrapper);
        return ResponseUtil.listRes(vips);
    }

    @ApiOperation("添加会员")
    @PostMapping("/add")
    public int create(@Valid @RequestBody Vip vip, HttpServletRequest request) {
        User user = JwtUtils.getSub(request);
        vip.setDeptId(user.getDeptId());
        vip.setDeptName(user.getDeptName());
        vipService.save(vip);
        return vip.getId();
    }

    @ApiOperation("修改会员")
    @PutMapping("/update")
    public void update(@Valid @RequestBody Vip vip) {
        vipService.updateById(vip);
    }


    @ApiOperation("删除会员")
    @DeleteMapping("/delete")
    public void delete(@RequestParam(value = "id")Integer id) {
        vipService.removeById(id);
    }

}
