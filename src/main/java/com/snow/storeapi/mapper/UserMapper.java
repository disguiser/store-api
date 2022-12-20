package com.snow.storeapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.storeapi.entity.User;

public interface UserMapper extends BaseMapper<User> {

    User selectByAccountName(String accountName);

}