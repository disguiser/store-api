package com.snow.storeapi.mapper;

import com.snow.storeapi.entity.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhou
 * @since 2018-10-20
 */
public interface UserRoleMapper {
    List<Role> selectByUserId(Integer userId);
    Integer test(Integer userId);
}
