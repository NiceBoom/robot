package com.fly.robot.service;

import com.fly.robot.entity.User;

public interface UserService {
    /**
     * 使用阿里云短信发送注册账户短信验证码
     *
     * @param phoneNumber 手机号
     */
    String sendRegisterMsgAuthCode(String phoneNumber) throws Exception;

    /**
     * 使用阿里云短信发送登录短信验证码
     *
     * @param phoneNumber 手机号
     */
    String sendLoginMsgAuthCode(String phoneNumber) throws Exception;

    /**
     * 校验验证码
     */
    boolean verifyCode(String phoneNumber, String code);

    /**
     * 注册
     *
     * @param user
     * @return 创建状态返回代码 UserCode
     */
    String register(User user);

    /**
     * 用户名密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return jwtToken
     */
    String userLogin(String username, String password);

    /**
     * 验证码登录
     * @param phoneNumber
     * @return
     */
    String authCodeLogin(String phoneNumber) throws RuntimeException;
}
