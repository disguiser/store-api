package com.snow.storeapi.DTO.user;

import com.snow.storeapi.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UpdateUserDTO {
    private Integer id;

    private String userName;

    private String accountName;

    private String password;

    private String avatar;

    private Integer deptId;

    private List<String> roles;

    private String newPassword;

    private String oldPassword;

    public static User toUser(UpdateUserDTO updateUserDTO) {
        return User
                .builder()
                .id(updateUserDTO.getId())
                .userName(updateUserDTO.getUserName())
                .accountName(updateUserDTO.getAccountName())
                .password(updateUserDTO.getPassword())
                .avatar(updateUserDTO.getAvatar())
                .deptId(updateUserDTO.getDeptId())
                .roles(updateUserDTO.getRoles())
                .build();
    }
}
