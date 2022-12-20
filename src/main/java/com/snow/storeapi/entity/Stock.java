package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("stock")
public class Stock implements Serializable {
    private static final long serialVersionUID = 5327492187818698583L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer deptId;

    private Integer goodsId;

    private Integer color;

    private Integer size;

    private Integer currentStock;

    private LocalDateTime modifyTime;

    private LocalDateTime createTime;

    private Integer inputUser;

    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private Integer amount;

    public Stock() {

    }
}
