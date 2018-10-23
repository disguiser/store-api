package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhou
 * @since 2018-10-20
 */
@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String userName;

    private String roleId;

    private String roleName;

}
