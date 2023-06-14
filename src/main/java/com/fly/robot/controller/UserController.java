package com.fly.robot.controller;

import com.fly.robot.annotation.JwtAuthenticated;
import com.fly.robot.dao.UserRepository;
import com.fly.robot.entity.User;
import com.fly.robot.pojo.Result;
import com.fly.robot.pojo.StatusCode;
import com.fly.robot.pojo.UserCode;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final static String dir = "Working directory: " + System.getProperty("user.dir");
    @ApiOperation("发送验证码")
    @GetMapping("/sendAuthCode")
    void sendAuthCode(@RequestParam("phone") String phone) throws Exception {
        userService.sendAuthCode(phone);
    }

    @ApiOperation("新建用户")
    @PostMapping("/register")
    Result register(@RequestBody User user, @RequestParam("authCode") String authCode) {
        if (userService.verifyCode(user.getPhone(), authCode)) {
            String register = userService.register(user);
            if (UserCode.CREATE_USER_ERROR_PHONE_NUMBER_REPETITION.equals(register))
                return new Result<>(false, StatusCode.ERROR, "该手机号手机号已被注册");
            if (UserCode.CREATE_USER_ERROR_USER_NAME_REPETITION.equals(register))
                return new Result<>(false, StatusCode.ERROR, "用户名重复，请重试");
            if (UserCode.CREATE_USER_ERROR_EMAIL_REPETITION.equals(register))
                return new Result<>(false, StatusCode.ERROR, "该邮箱已被注册，请重试");
            return new Result(true, StatusCode.OK, "创建成功，请登录");
        }
        return new Result<>(false, StatusCode.ERROR, "验证码错误");

    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    Result login(@RequestBody User user) {
        String token = userService.login(user.getUsername(), user.getPassword());
        if (UserCode.LOGIN_USER_ERROR_PASSWORD_FAIL.equals(token))
            return new Result(false, StatusCode.ERROR, "登陆失败，账户或者密码错误");
        if (UserCode.LOGIN_USER_ERROR_ACCOUNT_FAIL.equals(token))
            return new Result(false, StatusCode.ERROR, "登陆失败，该账户已被禁用");
        return new Result(true, StatusCode.OK, "登陆成功", token);
    }

    @Autowired
    private UserRepository userRepository;

    //只有管理员 1 与超级管理员 2 能访问这里
    @PostMapping("/authTest")
    @JwtAuthenticated(value = {UserCode.ADMINISTRATOR_USER_AUTH, UserCode.SUPER_ADMINISTRATOR_USER_AUTH})
    String authTest(@RequestBody User user) {
        User byUsername = userRepository.findByUsername(user.getUsername());
        LOGGER.info(byUsername.toString());
        return byUsername.getPermission().toString();
    }
}
