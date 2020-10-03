package com.snow.storeapi.mapper;

import com.snow.storeapi.entity.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictMapper {
    Integer insert(Dict dict);
    void deleteById(Integer id);
    Integer updateById(Dict dict);
    List<Dict> selectAll(@Param("dictName") String dictName, @Param("sort") String sort);
}
