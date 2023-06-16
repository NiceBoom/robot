package com.fly.robot.controller;

import com.fly.robot.annotation.JwtAuthenticated;
import com.fly.robot.api.CommonResult;
import com.fly.robot.dao.UserRepository;
import com.fly.robot.dto.PhoneLoginParam;
import com.fly.robot.dto.PhoneRegisterParam;
import com.fly.robot.dto.UpdateUserInfoParam;
import com.fly.robot.dto.UserLoginParam;
import com.fly.robot.entity.User;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/user")
@EnableScheduling
@Component
@Slf4j
@Api(tags = "用户控制")
public class UserController {

    @Autowired
    private UserService userService;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @ApiOperation("发送注册验证码")
    @GetMapping("/sendRegisterMsgAuthCode")
    CommonResult sendRegisterMsgAuthCode(@RequestParam("phone")
                                         @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号格式不正确")
                                         @NotEmpty(message = "手机号不能为空")
                                         String phone) {

        try {
            userService.sendRegisterMsgAuthCode(phone);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success("发送验证码成功！");
    }

    @ApiOperation("发送登录验证码")
    @GetMapping("/sendLoginMsgAuthCode")
    CommonResult sendLoginMsgAuthCode(@RequestParam("phone")
                                      @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号格式不正确")
                                      @NotEmpty(message = "手机号不能为空")
                                      String phone) {

        try {
            userService.sendLoginMsgAuthCode(phone);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success("发送验证码成功！");
    }


    @ApiOperation("手机号注册用户")
    @PostMapping("/phoneRegister")
    CommonResult phoneRegister(@Validated @RequestBody PhoneRegisterParam phoneRegisterParam) {
        try {
            userService.phoneRegister(phoneRegisterParam);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success("注册成功");
    }

    @ApiOperation("用户名密码登录")
    @PostMapping("/userLogin")
    CommonResult login(@Validated @RequestBody UserLoginParam userLoginParam) {
        try {
            return CommonResult.success(userService.userLogin(userLoginParam.getUsername(), userLoginParam.getPassword()));
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @ApiOperation("验证码登录")
    @PostMapping("/authCodeLogin")
    CommonResult authCodeLogin(@Validated @RequestBody PhoneLoginParam phoneLoginParam) {
        if (!userService.verifyCode(phoneLoginParam.getPhone(), phoneLoginParam.getAuthCode()))
            return CommonResult.failed("验证码错误，请重试。");
        try {
            return CommonResult
                    .success(userService.authCodeLogin(phoneLoginParam.getPhone()));
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @ApiOperation("查找个人用户信息")
    @PostMapping("/findUserInfoByToken")
    @JwtAuthenticated(value =
            {UserCode.REGULAR_USER_AUTH,
                    UserCode.ADMINISTRATOR_USER_AUTH,
                    UserCode.SUPER_ADMINISTRATOR_USER_AUTH})
    CommonResult findUserInfoByToken(@RequestHeader("Authorization") String token) {
        try {
            return CommonResult.success(userService.findUserInfoByToken(token));
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }


    @ApiOperation("修改用户信息")
    @PostMapping("/updateUserInfo")
    @JwtAuthenticated(value =
            {UserCode.REGULAR_USER_AUTH,
                    UserCode.ADMINISTRATOR_USER_AUTH,
                    UserCode.SUPER_ADMINISTRATOR_USER_AUTH})
    CommonResult updateUserInfo(@Validated @RequestBody UpdateUserInfoParam updateUserInfoParam,
                                @RequestHeader("Authorization") String token) {
        try {
            userService.updateUserInfo(updateUserInfoParam, token);
            return CommonResult.success("更新成功");
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
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
