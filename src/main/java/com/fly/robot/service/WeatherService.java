package com.fly.robot.service;


import com.fly.robot.pojo.TableAddressAdcode;
import com.fly.robot.pojo.WeatherDTO;

public interface WeatherService {

    /**
     *  从高德地图api查找消息地址的具体数据
     * @param getAddressInfoUrl 获取地址具体信息的URL
     * @param gaodeWebApiKey 高德ApiKey
     * @param findAddressInfoMsg 需要查找具体信息的地址
     * @return
     */
    TableAddressAdcode findAddressInfoByMsg(String getAddressInfoUrl, String gaodeWebApiKey, String findAddressInfoMsg) throws Exception;

    /**
     * 发送请求获取天气
     * @param APIkey     高德APIKey
     * @param cityCode   城市代码
     * @param extensions 实时天气或者天气预报代码
     * @return
     */
     WeatherDTO getWeather(String APIkey, String cityCode, String extensions) throws Exception;
}
