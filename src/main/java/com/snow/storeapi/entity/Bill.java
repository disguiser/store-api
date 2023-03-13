package com.snow.storeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author zhou
 * @date 2022/10/26 23:44
 */
@Data
@TableName(value = "bill")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bill implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer customerId;

    private Integer amount;

    private Integer paymentChannel;

    private LocalDate date;
}
