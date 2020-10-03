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

@Data
@Accessors(chain = true)
@TableName("goods")
public class Goods implements Serializable {
    private static final long serialVersionUID = 5327492187818698583L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String sku;

    @NonNull
    private String name;

    private String imgUrl;

    @NonNull
    private BigDecimal salePrice;

    private BigDecimal costPrice;

    private BigDecimal discount;

    private LocalDateTime modifyTime;

    private LocalDateTime createTime;

    private Integer deleted;

    @TableField(exist = false)
    private List<Stock> stocks;

    public Goods() {

    }
}