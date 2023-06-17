package com.fly.robot.service;

import com.fly.robot.dto.PhoneRegisterParam;
import com.fly.robot.dto.UpdateUserInfoParam;
import com.fly.robot.dto.UserInfoResultDTO;

public interface UserService {

    // 使用阿里云短信发送注册账户短信验证码
    void sendRegisterMsgAuthCode(String phoneNumber) throws Exception;

    //使用阿里云短信发送登录短信验证码
    void sendLoginMsgAuthCode(String phoneNumber) throws Exception;

    void phoneRegister(PhoneRegisterParam phoneRegisterParam);

    /**
     * 校验验证码
     *
     * @param phoneNumber 手机号
     * @param authCodeType 验证码类型
     * @param code        验证码
     * @return
     */
    boolean verifyCode(String phoneNumber, String authCodeType, String code);

    //用户名密码登录
    String userLogin(String username, String password);

    //验证码登录
    String authCodeLogin(String phoneNumber);

    //根据token查询该用户信息
    UserInfoResultDTO findUserInfoByToken(String token);

    //更新用户信息
    void updateUserInfo(UpdateUserInfoParam updateUserInfoParam, String token);
}
