package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@TableName("charge_record")
public class ConsumeRecord implements Serializable {
    private static final long serialVersionUID = -8539565840972649434L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NonNull
    private Integer vipId;

    @NonNull
    private BigDecimal consumeAmount;

    private Integer creator;
}
