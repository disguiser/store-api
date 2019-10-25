package com.snow.storeapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.entity.Vip;
import com.snow.storeapi.service.IVipService;
import com.snow.storeapi.util.JwtUtils;
import com.snow.storeapi.util.ResponseUtil;
import com.snow.storeapi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
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
    private IVipService vipInfoService;

    @GetMapping()
    public Map list(
            @RequestParam(value = "name", required = false)String name,
            @RequestParam(value = "phone", required = false)String phone,
            @RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit
    ) {
        IPage<Vip> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, limit);
        QueryWrapper<Vip> queryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        if (!StringUtil.isEmpty(phone)) {
            queryWrapper.eq("phone", phone);
        }
        IPage<Vip> vipInfos = vipInfoService.page(page, queryWrapper);
        return ResponseUtil.pageRes(vipInfos);
}

    @PostMapping()
    public Map<String, Integer> create(@Valid @RequestBody Vip vipInfo, HttpServletRequest request) {
        User userInfo = JwtUtils.getSub(request);
        vipInfo.setDeptId(userInfo.getDeptId());
        vipInfo.setDeptName(userInfo.getDeptName());
        vipInfoService.save(vipInfo);
        return new HashMap<String,Integer>(){{put("id", vipInfo.getId());}};
    }

    @PutMapping()
    public void update(@Valid @RequestBody Vip vipInfo, HttpServletRequest request) {
        User userInfo = JwtUtils.getSub(request);
        vipInfoService.update(vipInfo, new QueryWrapper<Vip>().eq("deptId", userInfo.getDeptId()));
    }

    @DeleteMapping()
    public void delete(HttpServletRequest request, @RequestParam(value = "vipInfoId")Integer vipInfoId) {
        User userInfo = JwtUtils.getSub(request);
        vipInfoService.remove(new QueryWrapper<Vip>()
                .eq("vipInfoId", vipInfoId)
                .eq("deptId", userInfo.getDeptId()));
    }
}
