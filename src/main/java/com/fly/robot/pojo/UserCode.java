package com.fly.robot.pojo;

public class UserCode {
    public static final String CREATE_USER_SUCCESS = "0";//创建用户成功
    public static final String CREATE_USER_ERROR_USER_NAME_REPETITION = "1";//创建用户失败，用户名已被注册
    public static final String CREATE_USER_ERROR_PHONE_NUMBER_REPETITION = "2";//创建用户失败，手机号已被注册
    public static final String CREATE_USER_ERROR_EMAIL_REPETITION = "3";//创建用户失败，邮箱已被注册
    public static final String CREATE_USER_ERROR_AUTH_CODE_ERROR = "4";//创建失败，验证码错误

    public static final Integer ACCOUNT_REGULAR = 0;//账户正常状态 0
    public static final Integer ACCOUNT_FAIL = 1;//账户非正常状态 1

    public static final Integer REGULAR_USER_AUTH = 0;//普通用户权限代码
    public static final int ADMINISTRATOR_USER_AUTH = 1;//管理员权限代码
    public static final int SUPER_ADMINISTRATOR_USER_AUTH = 2;//超级管理员权限代码

    public static final String LOGIN_USER_ERROR_PASSWORD_FAIL = "2";//密码不正确
    public static final String LOGIN_USER_ERROR_ACCOUNT_FAIL = "3";//账号已被禁用
}
