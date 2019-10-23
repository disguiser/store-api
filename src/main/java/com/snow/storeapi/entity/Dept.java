package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("dept_info")
public class Dept {
    private Integer id;

    private String name;
}