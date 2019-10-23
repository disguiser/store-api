package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("role_info")
public class Role {
    private Integer id;

    private String name;
}
