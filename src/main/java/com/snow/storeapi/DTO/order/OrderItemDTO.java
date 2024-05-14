package com.snow.storeapi.DTO.order;

import com.snow.storeapi.entity.OrderGoods;
import lombok.Data;

@Data
public class OrderItemDTO extends OrderGoods {
    private Integer currentStock;
}
