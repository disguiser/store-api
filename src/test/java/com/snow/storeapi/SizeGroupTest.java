package com.snow.storeapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.Dict;
import com.snow.storeapi.mapper.DictMapper;
import com.snow.storeapi.mapper.SizeGroupMapper;
import com.snow.storeapi.service.ISizeGroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SizeGroupTest {
    @Autowired
    private SizeGroupMapper sizeGroupMapper;
    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private ISizeGroupService sizeGroupService;

//    @Test
    public void testMapperSelect() {
        var sizeGroups = sizeGroupMapper.selectList(null);
        System.out.println(sizeGroups);
    }

//    @Test
    public void testServiceSelect() {
        var sizeGroups = sizeGroupService.findAll();
        System.out.println(sizeGroups);
    }

//    @Test
    public void listAll() {
        var dicts = dictMapper.selectOne(new QueryWrapper<Dict>().eq("dict_name", "尺码"));
        System.out.println(dicts.getData().get(0));
    }
}
