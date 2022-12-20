package com.snow.storeapi.service.impl;

import com.snow.storeapi.entity.Vip;
import com.snow.storeapi.mapper.VipMapper;
import com.snow.storeapi.service.IVipService;
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
public class VipServiceImpl extends ServiceImpl<VipMapper, Vip> implements IVipService {
}
