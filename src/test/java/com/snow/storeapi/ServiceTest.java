package com.snow.storeapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.entity.GoodsCategory;
import com.snow.storeapi.service.IGoodsCategoryService;
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
    private IGoodsCategoryService categoryService;

//    @Test
    public void testSelect() {
        GoodsCategory categories = categoryService.getOne(new QueryWrapper<GoodsCategory>().eq("name", "常服"));
        System.out.println(categories);
    }

    @Test
    @Transactional
    public void testInset() throws Exception {
        GoodsCategory category = new GoodsCategory();
        category.setName("test");
        category.setSizeGroup(new String[]{"31", "32"});
        categoryService.save(category);
//        throw new Exception();
    }
}
