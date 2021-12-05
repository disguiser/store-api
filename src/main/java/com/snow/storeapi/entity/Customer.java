package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName(value = "customer", autoResultMap = true)
public class Customer implements Serializable {
    private static final long serialVersionUID = 3598460335048159623L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String mobile;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> address;

    private String addressDetail;

    private String openId;

    private BigDecimal debt;

    private LocalDateTime createTime;
}
