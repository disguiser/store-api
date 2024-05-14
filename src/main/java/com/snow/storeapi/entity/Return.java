package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.snow.storeapi.DTO.returngoods.ReturnItemDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName("`return`")
@NoArgsConstructor
public class Return implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NonNull
    private Integer total;
    private Integer buyer;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT)
    private Integer createBy;
    @TableField(fill = FieldFill.INSERT)
    private Integer deptId;
    // goods may with stock
    @TableField(exist = false)
    private List<ReturnItemDTO> itemList;
    @TableLogic
    private Integer deleted;
}
