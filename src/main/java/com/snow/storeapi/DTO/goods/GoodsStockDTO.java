package com.snow.storeapi.DTO.goods;

import lombok.Data;

@Data
public class GoodsStockDTO {
    // goodsId
    private Integer id;

    private String sku;

    private String name;

    private Integer sizeGroup;

    private String imgUrl;

    private Integer salePrice;

    private Integer costPrice;

    private Integer color;

    private Integer size;

    private Integer currentStock;

    private String preSku;
}
