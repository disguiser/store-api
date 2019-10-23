package com.snow.storeapi.service.impl;

import com.snow.storeapi.entity.VipInfo;
import com.snow.storeapi.mapper.VipMapper;
import com.snow.storeapi.service.IVipInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhou
 * @since 2018-10-19
 */
@Service
public class VipInfoServiceImpl extends ServiceImpl<VipMapper, VipInfo> implements IVipInfoService {
}
