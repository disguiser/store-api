package com.snow.storeapi.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(value = "print_template", autoResultMap = true)
public class PrintTemplate {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer width;

    private Integer height;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONArray data;
}
