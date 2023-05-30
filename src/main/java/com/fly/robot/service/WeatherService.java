package com.fly.robot.service;

import com.fly.robot.entity.Result;

public interface WeatherService {

    /**
     *  从高德地图api查找消息地址的具体数据
     * @param getAddressInfoUrl 获取地址具体信息的URL
     * @param gaodeWebApiKey 高德ApiKey
     * @param findAddressInfoMsg 需要查找具体信息的地址
     * @return
     */
    Result findAddressInfoByMsg(String getAddressInfoUrl, String gaodeWebApiKey, String findAddressInfoMsg);

    /**
     * 发送请求获取天气
     * @param APIkey     请求地址链接
     * @param cityCode   城市代码
     * @param extensions 实时天气或者天气预报代码
     * @return
     */
     Result getWeather(String APIkey, String cityCode, String extensions) ;
}
