package com.fly.robot.service;

import com.fly.robot.entity.Result;

public interface FlyBookService {

    /**
     * 发送实时天气消息
     *
     * @param robotWebHookAddress 消息机器人web hook地址
     * @return
     */
    Result sendLiveWeatherMsg(String robotWebHookAddress, String cityId);

    /**
     * 发送未来天气预报消息
     *
     * @param robotWebHookAddress 消息机器人web hook地址
     * @return
     */
    Result sendForecastWeatherMsg(String robotWebHookAddress, String cityId);

    /**
     * @param getTokenAddress 获取TenantAccessTokenAddress链接地址
     * @param robotAppId
     * @param robotAppSecret
     * @param tokenType       获取token类型
     * @return
     */
    Result getToken(String getTokenAddress, String robotAppId, String robotAppSecret, String tokenType);

    /**
     * 发送查询地址的未来天气预报并@查询人
     *
     * @param sendMsgUrl         发送消息地址
     * @param tenantAccessToken
     * @param openId             查询人的OpenId
     * @param weatherDtoString 天气dtoString
     * @return
     */
    Result sendWeatherMsgToOpenId(String sendMsgUrl, String weatherCode, String tenantAccessToken, String openId, Object weatherDto) throws Exception;
}
