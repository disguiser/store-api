package com.snow.storeapi.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snow.storeapi.entity.ConsumeRecord;
import com.snow.storeapi.entity.PageResponse;
import com.snow.storeapi.entity.Vip;
import com.snow.storeapi.service.IConsumeRecordService;
import com.snow.storeapi.service.IVipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消费记录
 */
@RestController
@RequestMapping("/consume-record")
@RequiredArgsConstructor
public class ConsumeRecordController {
    private final IConsumeRecordService consumeRecordService;
    private final IVipService vipService;
    @GetMapping("/page")
    public ResponseEntity<?> list(
            @RequestParam(value = "vipId", required = false)String vipId,
            @RequestParam(value = "createDate", required = false)String createDate,
            @RequestParam(value = "page", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit
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
        /*
        if(!"".equals(user.getRoles())) {
            queryWrapper.eq("dept_id", user.getDeptId());
        }
        if (vipId != null) {
            queryWrapper.eq("vip_id", vipId);
        }*/
        queryWrapper.orderByDesc("create_time");
        IPage<ConsumeRecord> consumeRecords = consumeRecordService.page(page, queryWrapper);
        return ResponseEntity.ok(new PageResponse(consumeRecords.getTotal(), consumeRecords.getRecords()));
    }

    @PutMapping("")
    @Transactional(rollbackFor = Exception.class)
    public int create(@Valid @RequestBody ConsumeRecord consumeRecord) {
        consumeRecordService.save(consumeRecord);
        Vip vip = vipService.getById(consumeRecord.getVipId());
        Integer balance = vip.getBalance();
        Integer newBalance = balance - consumeRecord.getConsumeAmount();
        vip.setBalance(newBalance);
        vipService.updateById(vip);
        return consumeRecord.getId();
    }

    @DeleteMapping("")
    public void delete(@RequestParam(value = "ids") List<Integer> ids) {
        consumeRecordService.removeByIds(ids);
    }
}
