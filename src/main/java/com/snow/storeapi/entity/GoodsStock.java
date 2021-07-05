package com.snow.storeapi.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsStock {
    // goodsId
    private Integer id;

    private String sku;

    private String name;

    private Integer sizeGroup;

    private String imgUrl;

    private BigDecimal salePrice;

    private BigDecimal costPrice;

    private Integer color;

    private Integer size;

    private Integer currentStock;

    private String preSku;
}
