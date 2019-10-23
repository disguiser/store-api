package com.snow.storeapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.storeapi.entity.UserInfo;

public interface UserMapper extends BaseMapper<UserInfo> {

    UserInfo selectByLoginUser(String loginUser);

}