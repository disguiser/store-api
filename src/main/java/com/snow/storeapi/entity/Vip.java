package com.snow.storeapi.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

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
@TableName("app_vip")
public class Vip implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "vip_id", type = IdType.AUTO)
    private Integer vipId;
    @NotNull
    private String vipName;

    private SexEnum sex;
    @NotNull
    @Pattern(regexp = "^[0-9]{11}$")
    private String telephone;
    @Pattern(regexp = "^[1-2][0-9][0-9][0-9]-[0-1]{0,1}[0-9]-[0-3]{0,1}[0-9]$")
    private String birthday;

    private BigDecimal balance;

    private Integer score;

    private Integer deptId;

    private String deptName;

}
