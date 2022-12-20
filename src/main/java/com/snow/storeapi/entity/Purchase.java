package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("purchase")
public class Purchase implements Serializable {
    private static final long serialVersionUID = -6754062454872341742L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer stockId;

    private Integer purchaseAmount;

    private LocalDateTime createTime;
}
