package com.fly.robot.controller;

import com.fly.robot.entity.User;
import com.fly.robot.pojo.Result;
import com.fly.robot.pojo.StatusCode;
import com.fly.robot.pojo.UserCode;
import com.fly.robot.service.NoteService;
import com.fly.robot.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@EnableScheduling
@Api(tags = "FlyBookController")
@Component
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private NoteService noteService;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @ApiOperation("发送验证码")
    @GetMapping("/sendAuthCode")
    void sendAuthCode(@RequestParam("phone") String phone) throws Exception {
        noteService.sendAuthCode(phone);
    }

    @ApiOperation("新建用户")
    @PostMapping("/register")
    Result register(@RequestBody User user, @RequestParam("authCode") String authCode) {
        if(noteService.verifyCode(user.getPhone(),authCode)){
            userService.register(user);
            return new Result();
        }
        return new Result<>(false, StatusCode.ERROR,"创建错误，请重试");

    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    Result login(@RequestBody String username, String password) {
        String token = userService.login(username, password);
        return new Result(true, StatusCode.OK, "登陆成功", token);
    }

}
