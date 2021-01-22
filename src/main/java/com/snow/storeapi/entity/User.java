package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {
    private static final long serialVersionUID = 1711736924363048847L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotNull
    private String userName;

    @NotNull
    private String accountName;

    @NotNull
    private String password;

    private String avatar;

    @NotNull
    private Integer deptId;

    @NotNull
    private String deptName;

    @NotNull
    private String role;

    @NotNull
    private String status;

    private LocalDateTime modifyTime;

    private LocalDateTime createTime;

    @NotNull
    private String phoneNumber;

    @TableField(exist = false)
    private String newPassword;


    @TableField(exist = false)
    private String oldPassword;
}