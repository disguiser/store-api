package com.snow.storeapi.controller;

import com.alibaba.fastjson.JSON;
import com.snow.storeapi.constant.SystemConstant;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.service.IUserService;
import com.snow.storeapi.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    /*@ApiImplicitParams({
            @ApiImplicitParam(name = "loginUser", value = "用户名", required = true ,dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true ,dataType = "string")
    })*/
    @PostMapping(value = "/login")
    public ResponseEntity userLogin(@RequestBody User req) {
        User user = userService.selectByLoginUser(req.getLoginUser());
        if (user != null){
            if (user.getPassword().equals(req.getPassword())){
                Map<String, Object> sub = new HashMap<>();
                sub.put("userId", user.getUserId().toString());
                sub.put("userName", user.getUserName());
                sub.put("depts", user.getDeptName());
                String token = JwtUtils.createJWT(JSON.toJSONString(sub), SystemConstant.JWT_TTL);
                Map<String, Object> res = new HashMap<>();
                res.put("userId", user.getUserId());
                res.put("userName", user.getUserName());
                res.put("deptName", user.getDeptName());
                res.put("avatar", user.getAvatar());
                res.put("roles", user.getRoles());
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
}
