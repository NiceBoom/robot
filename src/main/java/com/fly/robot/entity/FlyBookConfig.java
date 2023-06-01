package com.fly.robot.entity;

/**
 * 飞书配置常量数据
 */
public class FlyBookConfig {

    //url
    public static final String GET_TENANT_ACCESS_TOKEN_ADDRESS =
            "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal";//获取tenant_access_token_address的url地址
    public static final String SEND_MSG_TO_USER_URL =
            "https://open.feishu.cn/open-apis/im/v1/messages";//获取给用户发送消息的url地址


    public static final String GET_FLYBOOK_TENANT_ACCESS_TOKEN = "tenant_access_token";//get tenant_access_token code
    public static final String GET_FLYBOOK_APP_ACCESS_TOKEN = "app_access_token";//get app_access_token code
    public static final String GET_FLYBOOK_USER_ACCESS_TOKEN = "user_access_token";//get user_access_token code
    public static final String SEND_LIVE_WEATHER_MSG_CODE = "send_live_weather_msg_code";
    public static final String SEND_FORECAST_WEATHER_MSG_CODE = "send_forecast_weather_code";
    public static final String SEARCH_FORECAST_WEATHER_MSG_CODE = "search_forecast_weather_code";
    public static final String SEARCH_LIVE_WEATHER_MSG_CODE = "search_live_weather_code";


    public static final String SEND_MSG_TEXT_TYPE = "text";

    public static final String YANG_GUANG_OPEN_ID = "ou_43204aba97d7eae211a3dac2e68e4a78";
    public static final String CHAT_ID = "oc_4c4e9549500b820ca27482cf088c919c";


}
