package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("stock")
@NoArgsConstructor
public class Stock implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer deptId;
    private Integer goodsId;
    private Integer color;
    private Integer size;
    private Integer currentStock;
    @TableField(exist = false)
    private Integer amount;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT)
    private Integer createBy;
    @TableLogic
    private Integer deleted;
}
