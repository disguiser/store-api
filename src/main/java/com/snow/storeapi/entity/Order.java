package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@TableName("`order`")
public class Order implements Serializable {
    private static final long serialVersionUID = 1562076273226551547L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NonNull
    private Integer total;

    private BigDecimal totalMoney;

    private LocalDateTime OrderTime;

    private Integer buyer;

    private Integer inputUser;

    @TableField(exist = false)
    private List<Map<String, Object>> stockList;

    private Integer deptId;

    private String deptName;

    private Integer category;

    private Integer paymentStatus;

    @TableLogic
    private Integer deleted;

    public Order() {
    }
}
