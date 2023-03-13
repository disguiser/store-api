package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

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
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @NotNull
    private String phoneNumber;
    private String phoneCode;
    private LocalDateTime codeExpTime;
}