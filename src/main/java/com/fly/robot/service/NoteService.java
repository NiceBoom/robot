package com.fly.robot.service;

public interface NoteService {
    /**
     * 使用阿里云短信发送短信验证码
     * @param phoneNumber 手机号
     */
    void sendAuthCode(String phoneNumber) throws Exception;
    /**
     * 校验验证码
     */
    boolean verifyCode(String phoneNumber, String code);
}
