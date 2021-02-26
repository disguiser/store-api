package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    private String color;

    private String size;

    private Integer currentStock;

    private LocalDateTime modifyTime;

    private LocalDateTime createTime;

    private Integer inputUser;

    private Integer deleted;

    @TableField(exist = false)
    private Integer amount;

    public Stock() {

    }
}
