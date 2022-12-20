package com.snow.storeapi.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DictItem {
    private Integer id;

    private String itemCode;

    private String itemName;
}
