package com.snow.storeapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.storeapi.entity.User;

public interface IUserService extends IService<User> {
    User selectByLoginUser(String loginUser);
}
