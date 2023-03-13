package com.snow.storeapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.mapper.UserMapper;
import com.snow.storeapi.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final UserMapper userMapper;

    public User loadUserByUsername(String accountName) throws Exception {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class).eq(User::getAccountName, accountName);
        User user = userMapper.selectOne(wrapper);
        return Optional.ofNullable(user)
//                .map(CustomerUserDetails::new)
                .orElseThrow(() -> new Exception("Username not found"));
    }
}
