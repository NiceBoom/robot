package com.fly.robot.service;

import com.fly.robot.entity.Result;
import com.fly.robot.pojo.TableFlybookToken;
import com.fly.robot.pojo.WeatherDTO;

public interface FlyBookService {

    /**
     * get flybook token
     * @param getTokenAddress
     * @param robotAppId
     * @param robotAppSecret
     * @param tokenType       获取token类型
     * @return
     */
    TableFlybookToken getToken(String getTokenAddress, String robotAppId, String robotAppSecret, String tokenType) throws Exception;

    /**
     * 发送天气消息到查询人
     * @param robotAppId         发送消息的机器人AppId
     * @param robotAppSecret     发送消息的机器人AppSecret
     * @param openId             消息接收人的openId
     * @param flyBookWeatherCode 发送天气类型代码
     * @param sendMsgType        发送消息类型代码
     * @param weatherDto         天气数据dto
     * @return
     * @throws Exception
     */
    Result sendWeatherMsgToOpenId(String robotAppId, String robotAppSecret, String openId, String flyBookWeatherCode, String sendMsgType, WeatherDTO weatherDto) throws Exception;

    /**
     * 发送天气消息到群聊中
     * @param robotWebHookAddress 群聊机器人发送消息 webhook地址
     * @param flyBookWeatherCode  天气类型代码
     * @param weatherDTO          天气数据
     * @return
     * @throws Exception
     */
    String sendDefaultCityWeatherMsgToGroupChat(String robotWebHookAddress, String flyBookWeatherCode, WeatherDTO weatherDTO) throws Exception;
}
