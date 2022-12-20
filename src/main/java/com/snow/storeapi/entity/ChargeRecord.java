package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("charge_record")
public class ChargeRecord implements Serializable {

    private static final long serialVersionUID = 6049091661620210844L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NonNull
    private Integer vipId;

    @NonNull
    private Integer chargeAmount;

    private Integer giveAmount;

    private Integer creator;

    private LocalDateTime createTime;
}
