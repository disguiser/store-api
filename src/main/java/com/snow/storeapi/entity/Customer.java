package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName(value = "customer", autoResultMap = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {
    private static final long serialVersionUID = 3598460335048159623L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String mobile;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private String[] address;

    private String addressDetail;

    private String openId;

    private Integer debt;

    private LocalDateTime createTime;
}
