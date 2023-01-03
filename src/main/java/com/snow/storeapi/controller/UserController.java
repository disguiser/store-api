package com.snow.storeapi.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snow.storeapi.DTO.user.UpdateUserDTO;
import com.snow.storeapi.DTO.user.UserLoginDTO;
import com.snow.storeapi.entity.ErrorMsg;
import com.snow.storeapi.entity.Sse;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.service.IUserService;
import com.snow.storeapi.util.JwtUtils;
import com.snow.storeapi.util.ResponseUtil;
import com.snow.storeapi.util.SMSUtil;
import com.xkzhangsan.time.calculator.DateTimeCalculatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Api(value = "UserController",  tags="用户管理")
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {
    private Map<String, Sse> sseEmitterMap = new ConcurrentHashMap<>();

    @Value("${SALT}")
    private String SALT;

    @Autowired
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public ResponseEntity userLogin(@RequestBody User req) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        var accountName = req.getAccountName();
        var phoneNumber = req.getPhoneNumber();
        if (!StrUtil.isEmpty(accountName)) {
            queryWrapper.eq("account_name", accountName);
        } else if (!StrUtil.isEmpty(phoneNumber)) {
            queryWrapper.eq("phone_number", phoneNumber);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        User userInfo = userService.getOne(queryWrapper);
        if (userInfo != null){
            var hexPw = DigestUtil.md5Hex(req.getPassword() + SALT);
            if (!StrUtil.isEmpty(accountName) && !userInfo.getPassword().equals(hexPw)){
                Map<String, Object> res = new HashMap<>();
                res.put("msg","密码不正确!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }
            if (!StrUtil.isEmpty(phoneNumber) &&
                (!userInfo.getPhoneCode().equals(req.getPhoneCode()) ||
                userInfo.getCodeExpTime().isBefore(LocalDateTime.now()))
            ) {
                Map<String, Object> res = new HashMap<>();
                res.put("msg","验证码不正确或已失效!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }
            return ResponseEntity.ok(buildLoginResponse(userInfo));
        } else {
//            Map<String, Object> res = new HashMap<>();
//            res.put("msg","不存在该用户!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg("不存在该用户!"));
        }
    }

    public UserLoginDTO buildLoginResponse(User userInfo) {
        var token = JwtUtils.generateToken(
                userInfo.getId(),
                userInfo.getUserName(),
                userInfo.getDeptId()
        );
        return UserLoginDTO
                .builder()
                .userName(userInfo.getUserName())
                .accountName(userInfo.getAccountName())
                .deptId(userInfo.getDeptId())
                .avatar(userInfo.getAvatar())
                .roles(userInfo.getRoles())
                .token(token)
                .build();
    }

    @ApiOperation("获取短信验证码")
    @GetMapping("/sendPhoneCode/{phoneNumber}")
    public ResponseEntity sendPhoneCode(@PathVariable String phoneNumber) {
        var userInfo = userService.getOne(
                new QueryWrapper<User>().lambda()
                        .eq(User::getPhoneNumber, phoneNumber)
        );
        if (userInfo != null) {
            var phoneCode = RandomUtil.randomNumbers(6);
            var codeExpTime = DateTimeCalculatorUtil.plusMinutes(LocalDateTime.now(), 20);
            var sign = "周明帅的网站";
            var templateId = "851052";
            var params = new String[]{phoneCode,"20"};
//            SMSUtil.sendMessage(templateId, sign, phoneNumber, params);
            userService.updateById(
                    userInfo
                        .setPhoneCode(phoneCode)
                        .setCodeExpTime(codeExpTime)
            );
            return ResponseEntity.ok(null);
        } else {
            var res = new HashMap<String, Object>();
            res.put("msg","该手机号码不存在!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
    }

    @ApiOperation("获取二维码")
    @GetMapping("/QRCode")
    public SseEmitter getBarcode(String clientId) {
        final SseEmitter emitter = new SseEmitter(0L);
        ExecutorService service = Executors.newSingleThreadExecutor();
        var result = new Sse();
        result.setClientId(clientId);
        result.setTimestamp(System.currentTimeMillis());
        result.setSseEmitter(emitter);
        sseEmitterMap.put(clientId, result);
        service.execute(() -> {
            for (;;) {
                try {
                    var serverId = UUID.randomUUID().toString();
                    sseEmitterMap.get(clientId).setServerId(serverId);
                    log.debug("sse send: " + serverId);
                    emitter.send(serverId);
//                    emitter.send(SseEmitter.event().name("complete").data(String.valueOf(System.currentTimeMillis())));
                    Thread.sleep(60_000L);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.debug("sse stop");
                    emitter.completeWithError(e);
                    break;
                }
            }
        });
        return emitter;
    }
    @ApiOperation("已扫码")
    @PostMapping("/scanned")
    public ResponseEntity scanned(@RequestBody Sse req) {
        var result = sseEmitterMap.get(req.getClientId());
        System.out.println(result.getServerId());
        System.out.println(req.getServerId());
        if (result.getServerId().equals(req.getServerId())) {
            try {
                result.getSseEmitter().send(SseEmitter.event().name("scanned").data("scanned"));
            } catch (IOException e) {
                log.debug("sacnned error");
                result.getSseEmitter().completeWithError(e);
            }
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(new ErrorMsg("二维码已过期"), HttpStatus.UNAUTHORIZED);
        }
    }
    @ApiOperation("手机端确认登录")
    @PostMapping("/phone-confirm")
    public void confirm(@RequestBody Sse req, HttpServletRequest request) {
        User user = JwtUtils.getSub(request);
        var result = sseEmitterMap.get(req.getClientId());
        if (result.getServerId().equals(req.getServerId())) {
            User userInfo = userService.getById(user.getId());
            var res = buildLoginResponse(userInfo);
            try {
                result.getSseEmitter()
                        .send(
                                SseEmitter.event()
                                        .name("confirm")
                                        .data(objectMapper.writeValueAsString(res))
                        );
            } catch (IOException e) {
                log.debug("phone confirm error");
                result.getSseEmitter().completeWithError(e);
            }
        }
    }

    @ApiOperation("用户列表查询")
    @GetMapping("/findByPage")
    public Map list(
            @RequestParam(value = "name", required = false)String name,
            @RequestParam(value = "accountName", required = false)String accountName,
            @RequestParam(value = "page", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit,
            HttpServletRequest request
    ) {
        User userInfo = JwtUtils.getSub(request);
        IPage<User> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, limit);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isEmpty(name)) {
            queryWrapper.eq("name", name);
        }
        if (!StrUtil.isEmpty(accountName)) {
            queryWrapper.eq("account_name", accountName);
        }
        //不是老板,只能查自己门店下的
        /*if(!"".equals(userInfo.getRole())) {
            queryWrapper.eq("dept_id", userInfo.getDeptId());
        }*/
        queryWrapper.orderByDesc("modify_time");
        IPage<User> usrInfos = userService.page(page, queryWrapper);
        return ResponseUtil.pageRes(usrInfos);
    }

    @ApiOperation("添加用户")
    @PutMapping("/create")
    public int addUser(@RequestBody User user){
        userService.save(user);
        return user.getId();
    }

    @ApiOperation("批量删除用户")
    @DeleteMapping("/delete")
    public void delete(@RequestParam(value = "ids") List<Integer> ids) {
        userService.removeByIds(ids);
    }


    @ApiOperation("更新用户")
    @PatchMapping("/update/{id}")
    public ResponseEntity update(@PathVariable Integer id,@RequestBody UpdateUserDTO updateUserDTO){
        if(!StrUtil.isEmpty(updateUserDTO.getNewPassword())){
            //校验旧密码是否一致
            User u = userService.getById(id);
            if(u.getPassword().equals(updateUserDTO.getOldPassword())) {
                updateUserDTO.setPassword(updateUserDTO.getNewPassword());
            }else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorMsg("旧密码不正确!"));
            }
        }
        userService.updateById(UpdateUserDTO.toUser(updateUserDTO));
        return ResponseEntity.ok("");
    }

    @ApiOperation("更新用户头像")
    @PatchMapping("/update-avatar/{id}")
    public ResponseEntity updateAvatar(@PathVariable Integer id, @RequestBody User user){
        Map<String, Object> res = new HashMap<>();
        var _user = new User();
        _user.setId(id);
        _user.setAvatar(user.getAvatar());
        userService.updateById(_user);
        return ResponseEntity.ok(res);
    }

    @ApiOperation("校验登录名唯一性")
    @GetMapping("/checkAccountNameUinque/{accountName}")
    public Boolean checkAccountNameUinque(@PathVariable String accountName){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_name",accountName);
        int count = userService.count(queryWrapper);
        if(count > 0){
            return false;
        }
        return true;
    }

    @GetMapping("/check-auth")
    public void checkAuth() {}
}
