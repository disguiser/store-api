package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName("goods")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Goods implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NonNull
    private String name;

    private Integer sizeGroup;

    private String imgUrl;

    @NonNull
    private Integer salePrice;

    private Integer wholesalePrice;

    private Integer costPrice;

    private String preSku;

    private Integer discount;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT)
    private Integer createBy;
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private List<Stock> stocks;

}
