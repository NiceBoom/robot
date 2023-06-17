package com.fly.robot.pojo;

public class UserCode {
    //用户基本状态代码
    public static final Integer USER_SEX_NON = 0;//用户性别未知
    public static final Integer USER_SEX_MAN = 1;//用户性别男
    public static final Integer USER_SEX_WOMEN = 2;//用户性别女

    public static final Integer USER_STATUS_ENABLE = 0;//用户状态正常
    public static final Integer USER_STATUS_DISABLE = 1;//用户被禁用

    //权限代码
    public static final int REGULAR_USER_AUTH = 0;//普通用户权限代码
    public static final int ADMINISTRATOR_USER_AUTH = 1;//管理员权限代码
    public static final int SUPER_ADMINISTRATOR_USER_AUTH = 2;//超级管理员权限代码

    //验证码类型
    public static final String REGISTER_AUTH = "register";//注册用户验证码类型
    public static final String LOGIN_AUTH = "login";//登录用户验证码类型
}
