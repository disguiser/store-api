package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@TableName("user_info")
public class UserInfo {
    private Integer id;

    private String name;

    private String loginUser;

    private String password;

    private String avatar;

    private Integer deptId;

    private String deptName;

    @TableField(exist = false)
    private String roleId;
    private String roleName;
}