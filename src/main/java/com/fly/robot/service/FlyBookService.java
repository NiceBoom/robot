package com.fly.robot.service;

import com.fly.robot.entity.Result;
import com.fly.robot.pojo.ForecastWeatherDTO;

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

    /**
     *  发送查询地址的未来天气预报并@查询人
     * @param sendMsgUrl 发送消息地址
     * @param tenantAccessToken
     * @param openId 查询人的OpenId
     * @param forecastWeatherDto 未来的天气预报信息
     * @param chatId 消息来源群组id
     * @return
     */
    Result sendForecastWeatherMsgToOpenId(String sendMsgUrl, String tenantAccessToken, String openId,String chatId, ForecastWeatherDTO forecastWeatherDto) throws Exception;
}
