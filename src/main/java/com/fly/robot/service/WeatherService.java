package com.fly.robot.service;


import com.fly.robot.entity.AddressAdcode;
import com.fly.robot.dto.WeatherDTO;

public interface WeatherService {

    /**
     * 从高德地图api查找消息地址的具体数据
     *
     * @param findAddressInfoMsg 需要查找具体信息的地址
     * @return
     */
    AddressAdcode findAddressInfoByMsg(String findAddressInfoMsg) throws Exception;

    /**
     * 发送请求获取天气
     *
     * @param cityCode   城市代码
     * @param extensions 实时天气或者天气预报代码
     * @return
     */
    WeatherDTO getWeather(String cityCode, String extensions) throws Exception;
}
