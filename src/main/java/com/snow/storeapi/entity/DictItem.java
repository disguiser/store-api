package com.snow.storeapi.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DictItem {

    private  String itemCode;

    private String itemName;
}
