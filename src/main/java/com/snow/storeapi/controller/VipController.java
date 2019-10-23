package com.snow.storeapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snow.storeapi.entity.UserInfo;
import com.snow.storeapi.entity.VipInfo;
import com.snow.storeapi.service.IVipInfoService;
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
@RequestMapping("/vipInfo")
public class VipInfoController {

    private static final Logger logger = LoggerFactory.getLogger(VipInfoController.class);

    @Autowired
    private IVipInfoService vipInfoService;

    @GetMapping()
    public Map list(
            @RequestParam(value = "name", required = false)String name,
            @RequestParam(value = "phone", required = false)String phone,
            @RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit
    ) {
        IPage<VipInfo> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, limit);
        QueryWrapper<VipInfo> queryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        if (!StringUtil.isEmpty(phone)) {
            queryWrapper.eq("phone", phone);
        }
        IPage<VipInfo> vipInfos = vipInfoService.page(page, queryWrapper);
        return ResponseUtil.pageRes(vipInfos);
    }

    @PostMapping()
    public Map<String, Integer> create(@Valid @RequestBody VipInfo vipInfo, HttpServletRequest request) {
        UserInfo userInfo = JwtUtils.getSub(request);
        vipInfo.setDeptId(userInfo.getDeptId());
        vipInfo.setDeptName(userInfo.getDeptName());
        vipInfoService.save(vipInfo);
        return new HashMap<String,Integer>(){{put("id", vipInfo.getId());}};
    }

    @PutMapping()
    public void update(@Valid @RequestBody VipInfo vipInfo, HttpServletRequest request) {
        UserInfo userInfo = JwtUtils.getSub(request);
        vipInfoService.update(vipInfo, new QueryWrapper<VipInfo>().eq("deptId", userInfo.getDeptId()));
    }

    @DeleteMapping()
    public void delete(HttpServletRequest request, @RequestParam(value = "vipInfoId")Integer vipInfoId) {
        UserInfo userInfo = JwtUtils.getSub(request);
        vipInfoService.remove(new QueryWrapper<VipInfo>()
                .eq("vipInfoId", vipInfoId)
                .eq("deptId", userInfo.getDeptId()));
    }
}
