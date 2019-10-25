package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("dept_info")
public class Dept implements Serializable {
    private static final long serialVersionUID = -4531867696279117971L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NonNull
    private String name;
}