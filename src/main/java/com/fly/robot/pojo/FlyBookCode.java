package com.fly.robot.pojo;

/**
 * 飞书配置常量数据
 */
public class FlyBookCode {

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

    //接收消息用户id类型 https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/message/create
    public static final String MSG_RECEIVE_ID_TYPE_OPEN_ID = "open_id";
    public static final String MSG_RECEIVE_ID_TYPE_USER_ID = "user_id";
    public static final String MSG_RECEIVE_ID_TYPE_UNION_ID = "union_id";
    public static final String MSG_RECEIVE_ID_TYPE_EMAIL = "email";
    public static final String MSG_RECEIVE_ID_TYPE_CHAT_ID = "chat_id";


    public static final String SEND_MSG_TEXT_TYPE = "text";


    //当查询天气消息格式错误，返回的消息内容。
    public static final String SEND_ERROR_WEATHER_MSG =
            "您发送的天气消息有误，请重试。可识别的查询格式为：城市，天气类型，天气。" +
                    "其中城市为您想查询的城市名称，天气类型为实时或者未来，实时查询的是当前天气预报，未来查询的是未来几天天气预报，最后一项天气为识别码" +
                    "例：北京，未来，天气。查询的是北京未来几天天气预报";


    public static final String YANG_GUANG_OPEN_ID = "ou_43204aba97d7eae211a3dac2e68e4a78";
    public static final String CHAT_ID = "oc_4c4e9549500b820ca27482cf088c919c";


}
