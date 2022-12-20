package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@TableName(value = "print_template", autoResultMap = true)
public class PrintTemplate {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer width;

    private Integer height;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Map<String, ?>> data;
}
