package com.snow.storeapi.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.snow.storeapi.enums.SexEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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

    private static final long serialVersionUID = 1L;

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

    private BigDecimal balance;

    private Integer deptId;

    private String deptName;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;
}
