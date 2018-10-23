package com.snow.storeapi.service.impl;

import com.snow.storeapi.entity.Role;
import com.snow.storeapi.mapper.RoleMapper;
import com.snow.storeapi.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhou
 * @since 2018-10-20
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
