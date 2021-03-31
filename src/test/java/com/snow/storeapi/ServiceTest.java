package com.snow.storeapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.SizeGroup;
import com.snow.storeapi.service.ISizeGroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {
    @Autowired
    private ISizeGroupService categoryService;

//    @Test
    public void testSelect() {
        SizeGroup categories = categoryService.getOne(new QueryWrapper<SizeGroup>().eq("name", "常服"));
        System.out.println(categories);
    }

//    @Test
//    @Transactional
    public void testInset() throws Exception {
        SizeGroup category = new SizeGroup();
        category.setName("test");
        categoryService.save(category);
//        throw new Exception();
    }
}
