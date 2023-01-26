package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "user", autoResultMap = true)
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

    @Singular
    @NotNull
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> roles;

    @NotNull
    private String status;

    private LocalDateTime modifyTime;

    private LocalDateTime createTime;

//    @TableField(exist = false)
//    private String newPassword;
//
//    @TableField(exist = false)
//    private String oldPassword;

    @NotNull
    private String phoneNumber;

    private String phoneCode;

    private LocalDateTime codeExpTime;
}