package com.fly.robot.service;

import com.fly.robot.entity.Result;

public interface FlyBookService {

    /**
     * 发送实时天气消息
     * @param robotWebHookAddress 消息机器人web hook地址
     * @return
     */
    Result sendLiveWeatherMsg(String robotWebHookAddress);

    /**
     * 发送未来天气预报消息
     * @param robotWebHookAddress 消息机器人web hook地址
     * @return
     */
    Result sendForecastWeatherMsg(String robotWebHookAddress);

    /**
     * @param getTenantAccessTokenAddress 获取TenantAccessTokenAddress链接地址
     * @param robotAppId  
     * @param robotAppSecret
     * @return
     */
    Result getTenantAccessToken(String getTenantAccessTokenAddress, String robotAppId, String robotAppSecret);
}
