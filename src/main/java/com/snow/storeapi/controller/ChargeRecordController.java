package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.ChargeRecord;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.entity.Vip;
import com.snow.storeapi.service.IChargeRecordService;
import com.snow.storeapi.service.IVipService;
import com.snow.storeapi.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 充值记录
 */
@RestController
@RequestMapping("/chargeRecord")
@RequiredArgsConstructor
public class ChargeRecordController {
    private final IChargeRecordService chargeRecordService;
    private final IVipService vipService;
    @ApiOperation("列表查询")
    @GetMapping("/page")
    public Map list(
            @RequestParam(value = "vipId", required = false)String vipId,
            @RequestParam(value = "startDate", required = false)String startDate,
            @RequestParam(value = "endDate", required = false)String endDate,
            @RequestParam(value = "page", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit,
            HttpServletRequest request
    ) {
        IPage<ChargeRecord> page = new Page<>(pageNum, limit);
        QueryWrapper<ChargeRecord> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isEmpty(vipId)) {
            queryWrapper.eq("vip_id", vipId);
        }
        if (!StrUtil.isEmpty(startDate) && !StrUtil.isEmpty(endDate)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            queryWrapper.between("create_time", startDate,endDate);
        }
        /*User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //不是老板,只能查自己门店下的
        if(!"".equals(user.getRoles())) {
            queryWrapper.eq("dept_id", user.getDeptId());
        }
        if (vipId != null) {
            queryWrapper.eq("vip_id", vipId);
        }*/
        queryWrapper.orderByDesc("create_time");
        IPage<ChargeRecord> chargeRecords = chargeRecordService.page(page, queryWrapper);
        return ResponseUtil.pageRes(chargeRecords);
    }

    @ApiOperation("添加")
    @PutMapping("")
    @Transactional(rollbackFor = Exception.class)
    public int create(@Valid @RequestBody ChargeRecord chargeRecord) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        chargeRecord.setCreator(user.getId());
        chargeRecordService.save(chargeRecord);
        Vip vip = vipService.getById(chargeRecord.getVipId());
        Integer balance = vip.getBalance();
        Integer newBalance = balance + chargeRecord.getChargeAmount() + chargeRecord.getGiveAmount();
        vip.setBalance(newBalance);
        vipService.updateById(vip);
        return chargeRecord.getId();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("")
    public void delete(@RequestParam(value = "ids") List<Integer> ids) {
        chargeRecordService.removeByIds(ids);
    }
}
