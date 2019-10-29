package com.snow.storeapi.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snow.storeapi.constant.SystemConstant;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.service.IUserService;
import com.snow.storeapi.util.JwtUtils;
import com.snow.storeapi.util.ResponseUtil;
import com.snow.storeapi.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(value = "UserController",  tags="用户管理")
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public ResponseEntity userLogin(@RequestBody User req) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_name",req.getAccountName());
        User userInfo = userService.getOne(queryWrapper);
        if (userInfo != null){
            if (userInfo.getPassword().equals(req.getPassword())){
                Map<String, Object> sub = new HashMap<>();
                sub.put("userId", userInfo.getId().toString());
                sub.put("userName", userInfo.getUserName());
                sub.put("depts", userInfo.getDeptName());
                String token = JwtUtils.createJWT(JSON.toJSONString(sub), SystemConstant.JWT_TTL);
                Map<String, Object> res = new HashMap<>();
                res.put("userId", userInfo.getId());
                res.put("userName", userInfo.getUserName());
                res.put("accountName", userInfo.getAccountName());
                res.put("deptName", userInfo.getDeptName());
                res.put("avatar", userInfo.getAvatar());
                res.put("role", userInfo.getRole());
                res.put("token", token);
                return ResponseEntity.ok(res);
            }else {
                Map<String, Object> res = new HashMap<>();
                res.put("msg","密码不正确!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }
        }else {
            Map<String, Object> res = new HashMap<>();
            res.put("msg","不存在该用户!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
    }

    @ApiOperation("用户列表查询")
    @GetMapping("/list")
    public Map list(
            @RequestParam(value = "name", required = false)String name,
            @RequestParam(value = "accountName", required = false)String accountName,
            @RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit,
            HttpServletRequest request
    ) {
        User userInfo = JwtUtils.getSub(request);
        IPage<User> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, limit);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        if (!StringUtil.isEmpty(accountName)) {
            queryWrapper.eq("account_name", accountName);
        }
        //不是老板,只能查自己门店下的
        /*if(!"".equals(userInfo.getRole())) {
            queryWrapper.eq("dept_id", userInfo.getDeptId());
        }*/
        IPage<User> usrInfos = userService.page(page, queryWrapper);
        return ResponseUtil.pageRes(usrInfos);
    }

    @ApiOperation("添加用户")
    @PostMapping("/add")
    public int addUser(@RequestBody User user, HttpServletRequest request){
        User req = JwtUtils.getSub(request);
        user.setDeptId(req.getDeptId());
        user.setDeptName(req.getDeptName());
        userService.save(user);
        return user.getId();
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/delete")
    public void delete(@RequestParam(value = "id")Integer id) {
        userService.removeById(id);
    }

    @ApiOperation("修改密码")
    @PutMapping("/updatePassword")
    public void updatePassword(@RequestParam(value = "newPassword") String password, HttpServletRequest request) {
        User user = JwtUtils.getSub(request);
        User userInfo = new User();
        userInfo.setId(user.getId());
        userInfo.setPassword(password);
        userService.updateById(userInfo);
    }
}