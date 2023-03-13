package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 店铺vip
 * </p>
 *
 * @author zhou
 * @since 2018-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vip")
public class Vip implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    @Pattern(regexp = "^[0-9]{11}$")
    private String phone;
    @NotNull
    @Pattern(regexp = "^[1-2][0-9][0-9][0-9]-[0-1]{0,1}[0-9]-[0-3]{0,1}[0-9]$")
    private String birthday;
    private String certNo;
    @NotNull
    private Integer birthDiscount;
    @NotNull
    private Integer vipDiscount;
    private Integer balance;
    @TableField(fill = FieldFill.INSERT)
    private Integer deptId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
