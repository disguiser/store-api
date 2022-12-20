package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.snow.storeapi.config.DictItemTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName(value = "sys_dict", autoResultMap = true)
public class Dict {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String dictName;

    @TableField(typeHandler = DictItemTypeHandler.class)
    private List<DictItem> data;

    private Boolean moreOption;

    private LocalDateTime modifyTime;
}
