package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@TableName("customer")
public class Customer implements Serializable {
    private static final long serialVersionUID = 3598460335048159623L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String mobile;

    private String address;

    private String openId;

}
