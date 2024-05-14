package com.snow.storeapi.DTO.returngoods;

import com.snow.storeapi.entity.ReturnGoods;
import lombok.Data;

@Data
public class ReturnItemDTO extends ReturnGoods {
    private Integer currentStock;
}
