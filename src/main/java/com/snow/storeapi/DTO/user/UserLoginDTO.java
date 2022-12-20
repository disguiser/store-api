package com.snow.storeapi.DTO.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserLoginDTO {
    private Integer id;

    private String userName;

    private String accountName;

    private String password;

    private String avatar;

    private Integer deptId;

    private List<String> roles;

    private String token;
}
