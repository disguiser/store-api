package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName(value = "size_group", autoResultMap = true)
public class SizeGroup implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<?> data;
}
