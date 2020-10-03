package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName("dept_info")
public class Dict {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String dictName;

    private List<DictItem> data;

    private Boolean ifUseCode;

    private LocalDateTime modifyTime;
}
