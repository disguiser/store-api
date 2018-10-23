package com.snow.storeapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.Vip;
import com.snow.storeapi.enums.SexEnum;
import com.snow.storeapi.service.IVipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VipMapperTest {
    @Autowired
    private IVipService vipService;

//    @Test
    public void testSelect() {
        Page<Vip> page = new Page<>(1, 2);
        IPage<Vip> vips = vipService.page(page, new QueryWrapper<Vip>().eq("dept_id", 101));
        System.out.println("总条数 ------> " + vips.getTotal());
        System.out.println("当前页数 ------> " + vips.getCurrent());
        System.out.println("当前每页显示数 ------> " + vips.getSize());
        System.out.println(vips.getRecords());
    }
//    @Test
    public void testInsert() {
        Vip vip = new Vip();
        vip.setBalance(new BigDecimal(100));
        vip.setBirthday("2011-01-01");
        vip.setDeptId(101);
        vip.setTelephone("18758222222");
        vip.setDeptName("女装");
        vip.setVipName("test");
        vip.setScore(20);
        vip.setSex(SexEnum.MAN);
        vipService.save(vip);
        System.out.println(vip);
    }
    @Test
    public void testDel() {
        vipService.remove(new QueryWrapper<Vip>().eq("vip_name", "test"));
    }
}
