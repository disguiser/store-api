package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snow.storeapi.entity.ChargeRecord;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.service.IChargeRecordService;
import com.snow.storeapi.util.JwtUtils;
import com.snow.storeapi.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * 充值记录
 */
@RestController
@RequestMapping("/chargeRecord")
public class ChargeRecordController {

    private static final Logger logger = LoggerFactory.getLogger(ChargeRecordController.class);
    
    @Autowired
    private IChargeRecordService chargeRecordService;

    @ApiOperation("列表查询")
    @GetMapping("/list")
    public Map list(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit,
            HttpServletRequest request
    ) {
        IPage<ChargeRecord> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, limit);
        QueryWrapper<ChargeRecord> queryWrapper = new QueryWrapper<>();
        User user = JwtUtils.getSub(request);
        //不是老板,只能查自己门店下的
        /*if(!"".equals(user.getRole())) {
            queryWrapper.eq("dept_id", user.getDeptId());
        }
        if (vipId != null) {
            queryWrapper.eq("vip_id", vipId);
        }*/
        IPage<ChargeRecord> chargeRecords = chargeRecordService.page(page, queryWrapper);
        return ResponseUtil.pageRes(chargeRecords);
    }

    @ApiOperation("添加")
    @PostMapping("/add")
    public int create(@Valid @RequestBody ChargeRecord chargeRecord,
                      HttpServletRequest request) {
        User user = JwtUtils.getSub(request);
        chargeRecord.setCreator(user.getId());
        chargeRecordService.save(chargeRecord);
        return chargeRecord.getId();
    }


    @ApiOperation("删除")
    @DeleteMapping("/delete")
    public void delete(@RequestParam(value = "id")Integer id) {
        chargeRecordService.removeById(id);
    }
}