package com.fly.robot.pojo;

public class UserCode {
    //用户基本状态代码
    public static final Integer USER_SEX_NON = 0;//用户性别未知
    public static final Integer USER_SEX_MAN = 1;//用户性别男
    public static final Integer USER_SEX_WOMEN = 2;//用户性别女

    public static final Integer USER_STATUS_ENABLE = 0;//用户状态正常
    public static final Integer USER_STATUS_DISABLE = 1;//用户被禁用

    public static final Integer ACCOUNT_FAIL = 1;//账户非正常状态 1
    public static final String LOGIN_USER_ERROR_PASSWORD_FAIL = "2";//密码不正确
    public static final String LOGIN_USER_ERROR_ACCOUNT_FAIL = "3";//账号已被禁用


    //权限代码
    public static final int REGULAR_USER_AUTH = 0;//普通用户权限代码
    public static final int ADMINISTRATOR_USER_AUTH = 1;//管理员权限代码
    public static final int SUPER_ADMINISTRATOR_USER_AUTH = 2;//超级管理员权限代码

    //发送验证码代码
    public static final String SEND_AUTH_CODE_MSG_SUCCESS = "0";//发送验证码成功
    public static final String SEND_AUTH_CODE_MSG_FAIL = "1";//发送验证码失败
    public static final String SEND_REGISTER_AUTH_CODE_FAIL_ACCOUNT_READY = "2";//发送注册验证码失败，该账户已被注册，请登录
    public static final String SEND_LOGIN_AUTH_CODE_FAIL_ACCOUNT_NOT_READY = "3";//发送登录验证码失败，该账户未被注册，请注册
}
