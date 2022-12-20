package com.snow.storeapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.storeapi.entity.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictMapper extends BaseMapper<Dict> {
//    Integer insert(Dict dict);
//    void deleteById(Integer id);
    Integer modifyById(Dict dict);
    List<Dict> selectAll(@Param("dictName") String dictName, @Param("sort") String sort, @Param("desc") Boolean desc);
}
