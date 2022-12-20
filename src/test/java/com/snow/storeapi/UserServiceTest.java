package com.snow.storeapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private IUserService userService;

//    @Test
    public void testSelect() {
        Page<User> page = new Page<>(1, 2);
        IPage<User> users = userService.page(page, new QueryWrapper<User>().eq("role", "Boss"));
        System.out.println("总条数 ------> " + users.getTotal());
        System.out.println("当前页数 ------> " + users.getCurrent());
        System.out.println("当前每页显示数 ------> " + users.getSize());
        System.out.println(users.getRecords());
    }
//    @Test
    public void testSelectOne() {
        User user = userService.getOne(new QueryWrapper<User>().eq("user_name", "test"));
        System.out.println(user);
    }
//    @Test
    public void testInsert() {
        User userInfo = new User();
        userInfo.setAccountName("test");
        userInfo.setUserName("test");
        var role = new ArrayList<String>();
        userInfo.setPassword("1");
        role.add("Waiter");
        role.add("Boss");
        userInfo.setRoles(role);
        userInfo.setStatus("Enabled");
        userService.save(userInfo);
        System.out.println(userInfo);
    }
//    @Test
    public void testDel() {
        userService.remove(new QueryWrapper<User>().eq("user_name", "test"));
    }
}
