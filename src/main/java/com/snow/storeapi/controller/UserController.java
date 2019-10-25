package com.snow.storeapi.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.storeapi.constant.SystemConstant;
import com.snow.storeapi.entity.R;
import com.snow.storeapi.entity.UserInfo;
import com.snow.storeapi.service.IUserService;
import com.snow.storeapi.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "UserController",  tags="用户管理")
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public ResponseEntity userLogin(@RequestBody UserInfo req) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_name",req.getAccountName());
        UserInfo userInfo = userService.getOne(queryWrapper);
        if (userInfo != null){
            if (userInfo.getPassword().equals(req.getPassword())){
                Map<String, Object> sub = new HashMap<>();
                sub.put("userId", userInfo.getId().toString());
                sub.put("userName", userInfo.getName());
                sub.put("depts", userInfo.getDeptName());
                String token = JwtUtils.createJWT(JSON.toJSONString(sub), SystemConstant.JWT_TTL);
                Map<String, Object> res = new HashMap<>();
                res.put("userId", userInfo.getId());
                res.put("userName", userInfo.getName());
                res.put("deptName", userInfo.getDeptName());
                res.put("avatar", userInfo.getAvatar());
                res.put("roleId", userInfo.getRoleId());
                res.put("roleName",userInfo.getRoleName());
                res.put("token", token);
                return ResponseEntity.ok(res);
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<String,String>(){{put("msg", "密码不正确!");}});
            }
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<String,String>(){{put("msg", "不存在该用户!");}});
        }
    }
    @GetMapping(value = "/menus")
    public List<Map<String, Object>> menus() {
        return null;
    }

    @ApiOperation("添加")
    @PostMapping("/add")
    public void addUser(@RequestBody UserInfo userInfo, HttpServletRequest request){
        UserInfo user = JwtUtils.getSub(request);
        userInfo.setDeptId(user.getDeptId());
        userInfo.setDeptName(user.getDeptName());
        userService.save(userInfo);
    }
}
