package com.snow.storeapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.Vip;
import com.snow.storeapi.service.IVipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VipServiceTest {
    @Autowired
    private IVipService vipService;

    @Test
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
        Vip vipInfo = new Vip();
        vipInfo.setBalance(new BigDecimal(100));
        vipInfo.setBirthday("2011-01-01");
        vipInfo.setDeptId(101);
        vipInfo.setPhone("18758222222");
        vipInfo.setDeptName("女装");
        vipInfo.setName("test");
        vipInfo.setBirthDiscount(70);
        vipInfo.setVipDiscount(90);
        vipService.save(vipInfo);
        System.out.println(vipInfo);
    }
    @Test
    public void testDel() {
        vipService.remove(new QueryWrapper<Vip>().eq("name", "test"));
    }
}
