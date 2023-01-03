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
    private static final long serialVersionUID = 5327492187818698583L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String sku;

    @NonNull
    private String name;

    private Integer sizeGroup;

    private String imgUrl;

    @NonNull
    private Integer salePrice;

    private Integer costPrice;

    private String preSku;

    private Integer discount;

    private LocalDateTime modifyTime;

    private LocalDateTime createTime;

    private Integer inputUser;
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private List<Stock> stocks;

}
