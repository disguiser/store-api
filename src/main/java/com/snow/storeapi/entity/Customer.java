package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName(value = "customer", autoResultMap = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String mobile;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private String[] address;

    private String addressDetail;

    private String openId;

    private Integer debt;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
