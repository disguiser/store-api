package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@TableName("`order`")
@NoArgsConstructor
public class Order implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NonNull
    private Integer total;
    private Integer totalMoney;
    private Integer buyer;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT)
    private Integer createBy;
    @TableField(fill = FieldFill.INSERT)
    private Integer deptId;
    // goods may with stock
    @TableField(exist = false)
    private List<Map<String, Object>> itemList;
    private Integer category;
    private Integer paymentStatus;
    @TableLogic
    private Integer deleted;
}
