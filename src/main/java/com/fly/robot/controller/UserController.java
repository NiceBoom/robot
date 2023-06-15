package com.fly.robot.controller;

import com.fly.robot.annotation.JwtAuthenticated;
import com.fly.robot.api.CommonResult;
import com.fly.robot.dao.UserRepository;
import com.fly.robot.entity.User;
import com.fly.robot.pojo.UserCode;
import com.fly.robot.service.UserService;
import com.fly.robot.util.Utils;
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
    CommonResult sendRegisterMsgAuthCode(@RequestParam("phone") String phone) {
        if (phone == null || !Utils.isChinaPhoneNumber(phone))
            return CommonResult.failed("请输入正确的手机号");
        try {
            userService.sendRegisterMsgAuthCode(phone);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success("发送验证码成功！");
    }

    @ApiOperation("发送登录验证码")
    @GetMapping("/sendLoginMsgAuthCode")
    CommonResult sendLoginMsgAuthCode(@RequestParam("phone") String phone) {
        if (phone == null || !Utils.isChinaPhoneNumber(phone))
            return CommonResult.failed("请输入正确的手机号");
        try {
            userService.sendLoginMsgAuthCode(phone);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success("发送验证码成功！");
    }

    /**
     * 手机号注册用户，手机号必填否则无法请求
     *
     * @param user
     * @param authCode
     * @return
     */
    @ApiOperation("手机号注册用户")
    @PostMapping("/phoneRegister")
    CommonResult phoneRegister(@RequestBody User user, @RequestParam("authCode") String authCode) {
        //校验数据是否合格
        if (user.getPhone() == null || !Utils.isChinaPhoneNumber(user.getPhone()))
            return CommonResult.failed("请输入正确的手机号");
        if (user.getUsername() == null)
            return CommonResult.failed("用户名不能为空");
        if (authCode == null)
            return CommonResult.failed("请填入您的验证码");
        try {
            userService.phoneRegister(user, authCode);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success("注册成功");
    }

    @ApiOperation("用户名密码登录")
    @PostMapping("/userLogin")
    CommonResult login(@RequestBody User user) {
        if (user.getUsername() == null)
            return CommonResult.failed("用户名不能为空");
        if (user.getPassword() == null)
            return CommonResult.failed("密码不能为空");

        try {
            return CommonResult.success(userService.userLogin(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }

    }

    @ApiOperation("验证码登录")
    @PostMapping("/authCodeLogin")
    CommonResult authCodeLogin(@RequestParam("phone") String phone, @RequestParam("authCode") String authCode) {
        if (!userService.verifyCode(phone, authCode))
            return CommonResult.failed("验证码错误，请重试。");
        try {
            return CommonResult.success(userService.authCodeLogin(phone));
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
