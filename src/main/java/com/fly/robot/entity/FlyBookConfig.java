package com.fly.robot.entity;

/**
 * 飞书配置常量数据
 */
public class FlyBookConfig {

    //获取tenant_access_token_address的url地址
    public static final String GET_TENANT_ACCESS_TOKEN_ADDRESS="https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal";

    //获取给用户发送消息的url地址
    public static final String SEND_MSG_TO_USER_URL="https://open.feishu.cn/open-apis/im/v1/messages";
}
