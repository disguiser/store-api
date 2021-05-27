package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snow.storeapi.entity.ConsumeRecord;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.entity.Vip;
import com.snow.storeapi.service.IConsumeRecordService;
import com.snow.storeapi.service.IVipService;
import com.snow.storeapi.util.JwtUtils;
import com.snow.storeapi.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 消费记录
 */
@RestController
@RequestMapping("/consumeRecord")
public class ConsumeRecordController {

    private static final Logger logger = LoggerFactory.getLogger(ConsumeRecordController.class);
    
    @Autowired
    private IConsumeRecordService consumeRecordService;

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
        IPage<ConsumeRecord> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, limit);
        QueryWrapper<ConsumeRecord> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isEmpty(vipId)) {
            queryWrapper.eq("vip_id", vipId);
        }
        if (!StrUtil.isEmpty(createDate)) {
            queryWrapper.like("create_time", createDate);
        }
        //不是老板,只能查自己门店下的
        /*User user = JwtUtils.getSub(request);
        if(!"".equals(user.getRoles())) {
            queryWrapper.eq("dept_id", user.getDeptId());
        }
        if (vipId != null) {
            queryWrapper.eq("vip_id", vipId);
        }*/
        queryWrapper.orderByDesc("create_time");
        IPage<ConsumeRecord> consumeRecords = consumeRecordService.page(page, queryWrapper);
        return ResponseUtil.pageRes(consumeRecords);
    }

    @ApiOperation("添加")
    @PutMapping("/create")
    @Transactional
    public int create(@Valid @RequestBody ConsumeRecord consumeRecord,
                      HttpServletRequest request) {
        User user = JwtUtils.getSub(request);
        consumeRecord.setCreator(user.getId());
        consumeRecordService.save(consumeRecord);
        Vip vip = vipService.getById(consumeRecord.getVipId());
        BigDecimal balance = vip.getBalance();
        BigDecimal newBalance = balance.subtract(consumeRecord.getConsumeAmount());
        vip.setBalance(newBalance);
        vipService.updateById(vip);
        return consumeRecord.getId();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/delete")
    public void delete(@RequestParam(value = "ids") List<Integer> ids) {
        consumeRecordService.removeByIds(ids);
    }
}
