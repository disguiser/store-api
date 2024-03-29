package com.snow.storeapi.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snow.storeapi.DTO.user.UpdateUserDTO;
import com.snow.storeapi.DTO.user.UserLoginDTO;
import com.snow.storeapi.entity.ErrorMsg;
import com.snow.storeapi.entity.User;
import com.snow.storeapi.security.JwtComponent;
import com.snow.storeapi.service.impl.UserServiceImpl;
import com.xkzhangsan.time.calculator.DateTimeCalculatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {
//    private final Map<String, Sse> sseEmitterMap = new ConcurrentHashMap<>();
    private final UserServiceImpl userService;
    private final JwtComponent jwtComponent;

//    private final ObjectMapper objectMapper;
    private final String SALT;

    public UserController(
            UserServiceImpl userService,
            JwtComponent jwtComponent,
//            ObjectMapper objectMapper,
            @Value("${SALT}") String SALT
    ) {
        this.userService = userService;
        this.jwtComponent = jwtComponent;
//        this.objectMapper = objectMapper;
        this.SALT = SALT;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> userLogin(@RequestBody User loginDto) throws Exception {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        var accountName = loginDto.getAccountName();
        var phoneNumber = loginDto.getPhoneNumber();
        if (!StrUtil.isEmpty(accountName)) {
            queryWrapper.eq("account_name", accountName);
        } else if (!StrUtil.isEmpty(phoneNumber)) {
            queryWrapper.eq("phone_number", phoneNumber);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        User userInfo = userService.loadUserByUsername(accountName);
        if (userInfo != null){
//            var hexPw = passwordEncoder.encode(loginDto.getPassword());
            var hexPw = DigestUtil.md5Hex(loginDto.getPassword() + SALT);
            if (!StrUtil.isEmpty(accountName) && !userInfo.getPassword().equals(hexPw)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg("密码不正确!"));
            }
            if (!StrUtil.isEmpty(phoneNumber) &&
                (!userInfo.getPhoneCode().equals(loginDto.getPhoneCode()) ||
                userInfo.getCodeExpTime().isBefore(LocalDateTime.now()))
            ) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg("验证码不正确或已失效!"));
            }
            return ResponseEntity.ok(buildLoginResponse(userInfo));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg("不存在该用户!"));
        }
    }

    public UserLoginDTO buildLoginResponse(User userInfo) {
        Map<String, Object> sub = new HashMap<>();
        sub.put("id", userInfo.getId());
        sub.put("userName", userInfo.getUserName());
        sub.put("deptId", userInfo.getDeptId());
        sub.put("roles", userInfo.getRoles());
        var token = jwtComponent.generateToken(sub);
        return UserLoginDTO
                .builder()
                .id(userInfo.getId())
                .userName(userInfo.getUserName())
                .accountName(userInfo.getAccountName())
                .deptId(userInfo.getDeptId())
                .avatar(userInfo.getAvatar())
                .roles(userInfo.getRoles())
                .token(token)
                .build();
    }

    @GetMapping("/sendPhoneCode/{phoneNumber}")
    public ResponseEntity<?> sendPhoneCode(@PathVariable String phoneNumber) {
        var userInfo = userService.getOne(
                new QueryWrapper<User>().lambda()
                        .eq(User::getPhoneNumber, phoneNumber)
        );
        if (userInfo != null) {
            var phoneCode = RandomUtil.randomNumbers(6);
            var codeExpTime = DateTimeCalculatorUtil.plusMinutes(LocalDateTime.now(), 20);
//            var sign = "周明帅的网站";
//            var templateId = "851052";
//            var params = new String[]{phoneCode,"20"};
//            SMSUtil.sendMessage(templateId, sign, phoneNumber, params);
            userService.updateById(
                    userInfo
                        .setPhoneCode(phoneCode)
                        .setCodeExpTime(codeExpTime)
            );
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg("该手机号码不存在!"));
        }
    }

    // https://stackoverflow.com/questions/69974804/avoid-busy-waiting-warning-in-a-thread
//    @GetMapping("/QRCode")
//    public SseEmitter getBarcode(String clientId) {
//        final SseEmitter emitter = new SseEmitter(0L);
//        ExecutorService service = Executors.newSingleThreadExecutor();
//        var result = new Sse();
//        result.setClientId(clientId);
//        result.setTimestamp(System.currentTimeMillis());
//        result.setSseEmitter(emitter);
//        sseEmitterMap.put(clientId, result);
//        service.execute(() -> {
//            for (;;) {
//                try {
//                    var serverId = UUID.randomUUID().toString();
//                    sseEmitterMap.get(clientId).setServerId(serverId);
//                    log.debug("sse send: " + serverId);
//                    emitter.send(serverId);
////                    emitter.send(SseEmitter.event().name("complete").data(String.valueOf(System.currentTimeMillis())));
//                    Thread.sleep(60_000L);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    log.debug("sse stop");
//                    emitter.completeWithError(e);
//                    break;
//                }
//            }
//        });
//        return emitter;
//    }
//    @PostMapping("/scanned")
//    public ResponseEntity<?> scanned(@RequestBody Sse req) {
//        var result = sseEmitterMap.get(req.getClientId());
//        System.out.println(result.getServerId());
//        System.out.println(req.getServerId());
//        if (result.getServerId().equals(req.getServerId())) {
//            try {
//                result.getSseEmitter().send(SseEmitter.event().name("scanned").data("scanned"));
//            } catch (IOException e) {
//                log.debug("sacnned error");
//                result.getSseEmitter().completeWithError(e);
//            }
//            return ResponseEntity.ok("");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMsg("二维码已过期"));
//        }
//    }
//    @PostMapping("/phone-confirm")
//    public void confirm(@RequestBody Sse sse) {
//        User user = BaseContext.getCurrentUser();
//        var result = sseEmitterMap.get(sse.getClientId());
//        if (result.getServerId().equals(sse.getServerId())) {
//            User userInfo = userService.getById(user.getId());
//            // TO-DO
//            var res = buildLoginResponse(userInfo);
//            try {
//                result.getSseEmitter()
//                        .send(
//                                SseEmitter.event()
//                                        .name("confirm")
//                                        .data(objectMapper.writeValueAsString(res))
//                        );
//            } catch (IOException e) {
//                log.debug("phone confirm error");
//                result.getSseEmitter().completeWithError(e);
//            }
//        }
//    }

    @GetMapping("/findByPage")
    public ResponseEntity<?> list(
            @RequestParam(value = "name", required = false)String name,
            @RequestParam(value = "accountName", required = false)String accountName,
            @RequestParam(value = "page", defaultValue = "1")Integer pageNum,
            @RequestParam(value = "limit", defaultValue = "10")Integer limit
    ) {
        IPage<User> page = new Page<>(pageNum, limit);
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
        queryWrapper.orderByDesc("update_time");
        IPage<User> usrInfos = userService.page(page, queryWrapper);
        return ResponseEntity.ok(usrInfos);
    }

    @PostMapping("")
    public int addUser(@RequestBody User user){
        userService.save(user);
        return user.getId();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody UpdateUserDTO updateUserDTO){
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

    @PatchMapping("/avatar/{id}")
    public ResponseEntity<?> updateAvatar(@PathVariable Integer id, @RequestBody User user){
        Map<String, Object> res = new HashMap<>();
        var _user = new User();
        _user.setId(id);
        _user.setAvatar(user.getAvatar());
        userService.updateById(_user);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/checkAccountNameUinque/{accountName}")
    public Boolean checkAccountNameUinque(@PathVariable String accountName){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_name",accountName);
        long count = userService.count(queryWrapper);
        return count == 0;
    }

}
