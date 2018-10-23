package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.Role;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.mapper.UserMapper;
import com.snow.storeapi.mapper.UserRoleMapper;
import com.snow.storeapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    public User selectByLoginUser(String loginUser) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("login_user", loginUser));
        if (user != null) {
            List<Role> roles = userRoleMapper.selectByUserId(user.getUserId());
            user.setRoles(roles);
            return user;
        } else {
            return null;
        }
    }
}
