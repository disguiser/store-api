package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("charge_record")
public class ChargeRecord implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NonNull
    private Integer vipId;

    @NonNull
    private Integer chargeAmount;

    private Integer giveAmount;

    @TableField(fill = FieldFill.INSERT)
    private Integer createBy;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
