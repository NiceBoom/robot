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
@Api(tags = "UserController", description = "用户接口")
@Component
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @ApiOperation("发送注册验证码")
    @GetMapping("/sendRegisterMsgAuthCode")
    Result sendRegisterMsgAuthCode(@RequestParam("phone") String phone) throws Exception {
        String sendRegisterMsgAuthCode = userService.sendRegisterMsgAuthCode(phone);
        if (UserCode.SEND_AUTH_CODE_MSG_SUCCESS.equals(sendRegisterMsgAuthCode))
            return new Result(true, StatusCode.OK, "验证码发送成功");
        if (UserCode.SEND_REGISTER_AUTH_CODE_FAIL_ACCOUNT_READY.equals(sendRegisterMsgAuthCode))
            return new Result(false, StatusCode.ERROR, "该账户已被注册，请登录");
        if (UserCode.SEND_AUTH_CODE_MSG_FAIL.equals(sendRegisterMsgAuthCode)) {
            LOGGER.info("发送短息返回状态代码为：" + sendRegisterMsgAuthCode + "。短信发送失败，请检查发送短信三方服务是否正常");
            return new Result(false, StatusCode.ERROR, "短信发送失败，请重试");
        }
        return new Result();
    }

    @ApiOperation("发送登录验证码")
    @GetMapping("/sendLoginMsgAuthCode")
    Result sendLoginMsgAuthCode(@RequestParam("phone") String phone) throws Exception {
        String sendLoginMsgAuthCode = userService.sendLoginMsgAuthCode(phone);
        if (UserCode.SEND_AUTH_CODE_MSG_SUCCESS.equals(sendLoginMsgAuthCode))
            return new Result(true, StatusCode.OK, "验证码发送成功");
        if (UserCode.SEND_LOGIN_AUTH_CODE_FAIL_ACCOUNT_NOT_READY.equals(sendLoginMsgAuthCode))
            return new Result(false, StatusCode.ERROR, "该账户未被注册，请先注册");
        if (UserCode.SEND_AUTH_CODE_MSG_FAIL.equals(sendLoginMsgAuthCode)) {
            LOGGER.info("发送短息返回状态代码为：" + sendLoginMsgAuthCode + "。短信发送失败，请检查发送短信三方服务是否正常");
            return new Result(false, StatusCode.ERROR, "短信发送失败，请重试");
        }
        return new Result();
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

    @ApiOperation("用户名密码登录")
    @PostMapping("/userLogin")
    Result login(@RequestBody User user) {
        String token = userService.userLogin(user.getUsername(), user.getPassword());
        if (UserCode.LOGIN_USER_ERROR_PASSWORD_FAIL.equals(token))
            return new Result(false, StatusCode.ERROR, "登陆失败，账户或者密码错误");
        if (UserCode.LOGIN_USER_ERROR_ACCOUNT_FAIL.equals(token))
            return new Result(false, StatusCode.ERROR, "登陆失败，该账户已被禁用");
        return new Result(true, StatusCode.OK, "登陆成功", token);
    }

    @ApiOperation("验证码登录")
    @PostMapping("/authCodeLogin")
    Result authCodeLogin(@RequestParam("phone") String phone, @RequestParam("authCode") String authCode) {
        if (!userService.verifyCode(phone, authCode))
            return new Result(false, StatusCode.ERROR, "验证码错误");
        try {
            return new Result(true, StatusCode.OK, "登陆成功", userService.authCodeLogin(phone));
        } catch (RuntimeException e) {
            return new Result(false, StatusCode.ERROR, e.getMessage());
        }
    }


    @Autowired
    private UserRepository userRepository;

    //只有管理员 1 与超级管理员 2 能访问这里,并返回用户信息，测试鉴权用
    @PostMapping("/authTest")
    @JwtAuthenticated(value = {UserCode.ADMINISTRATOR_USER_AUTH, UserCode.SUPER_ADMINISTRATOR_USER_AUTH})
    String authTest(@RequestBody User user) {
        User byUsername = userRepository.findByUsername(user.getUsername());
        LOGGER.info(byUsername.toString());
        return byUsername.toString();
    }
}
