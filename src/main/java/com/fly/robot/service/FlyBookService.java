package com.fly.robot.service;

import com.fly.robot.entity.TableFlybookToken;
import com.fly.robot.pojo.Result;
import com.fly.robot.dto.WeatherDTO;

public interface FlyBookService {

    /**
     * 发送天气消息到查询人
     *
     * @param idType      id类型
     * @param id          id数据
     * @param sendMsgType 发送消息类型代码
     * @param msg         消息内容
     * @return
     * @throws Exception
     */
    Result sendWeatherMsgToOpenId(String idType, String id, String sendMsgType, String msg) throws Exception;

    /**
     * 发送天气消息到群聊中
     *
     * @param flyBookWeatherCode 天气类型代码
     * @param msg                消息内容
     * @return
     * @throws Exception
     */
    String sendDefaultCityWeatherMsgToGroupChat(String flyBookWeatherCode, String sendMsgType, String msg) throws Exception;

    TableFlybookToken getToken(String tokenType) throws Exception;
}
