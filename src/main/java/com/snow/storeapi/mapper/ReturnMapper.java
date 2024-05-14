package com.snow.storeapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.storeapi.entity.Return;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Mapper
@Component(value = "returnMapper")
public interface ReturnMapper extends BaseMapper<Return> {

    List<Map<String, ?>> findByPage(
            @Param("start")Integer start,
            @Param("end")Integer end,
            @Param("address")String address,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate")LocalDateTime endDate,
            @Param("customerName")String customerName
    );

    Integer count(
            @Param("address")String address,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate")LocalDateTime endDate,
            @Param("customerName")String customerName
    );

    List<Map<String,Object>> getDetailByReturnId(@Param("returnId")Integer returnId);

}
