package com.snow.storeapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.ChargeRecord;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.entity.Vip;
import com.snow.storeapi.service.IChargeRecordService;
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
import java.math.BigDecimal;
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

    @Autowired
    private IVipService vipService;

    @ApiOperation("列表查询")
    @GetMapping("/findByPage")
    public Map list(
            @RequestParam(value = "vipId", required = false)String vipId,
            @RequestParam(value = "createDate", required = false)String createDate,
            @RequestParam(value = "page", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit,
            HttpServletRequest request
    ) {
        IPage<ChargeRecord> page = new Page<>(pageNum, limit);
        QueryWrapper<ChargeRecord> queryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(vipId)) {
            queryWrapper.eq("vip_id", vipId);
        }
        if (!StringUtil.isEmpty(createDate)) {
            queryWrapper.like("create_time", createDate);
        }
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
    @PutMapping("/create")
    public int create(@Valid @RequestBody ChargeRecord chargeRecord,
                      HttpServletRequest request) {
        User user = JwtUtils.getSub(request);
        chargeRecord.setCreator(user.getId());
        chargeRecordService.save(chargeRecord);
        Vip vip = vipService.getById(chargeRecord.getVipId());
        BigDecimal balance = vip.getBalance();
        BigDecimal newBalance = balance.add(chargeRecord.getChargeAmount()).add(chargeRecord.getGiveAmount());
        vip.setBalance(newBalance);
        vipService.updateById(vip);
        return chargeRecord.getId();
    }


    @ApiOperation("删除")
    @DeleteMapping("/delete")
    public void delete(@RequestParam(value = "id")Integer id) {
        chargeRecordService.removeById(id);
    }
}
