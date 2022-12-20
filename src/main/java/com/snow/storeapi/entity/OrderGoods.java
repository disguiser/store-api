package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("order_goods")
public class OrderGoods implements Serializable {
    private static final long serialVersionUID = 1986775620366512211L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer orderId;

    private Integer stockId;

    private Integer amount;

    private Integer salePrice;

    private Integer subtotalMoney;

    private Integer deleted;
}
