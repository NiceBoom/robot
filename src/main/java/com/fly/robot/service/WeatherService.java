package com.fly.robot.service;

import com.fly.robot.entity.Result;

public interface WeatherService {

    /**
     * 获取实时天气并保存到mysql中
     * @param weatherApiKey 天气apiKey
     * @param cityCode  城市代码
     * @param weatherCode 天气代码（实时天气或者未来天气预报）
     * @return
     */
    Result findLiveWeatherSaveToMysql(String weatherApiKey, String cityCode, String weatherCode);

    /**
     * 获取天气预报并保存到mysql中
     * @param weatherApiKey 天气apikey
     * @param cityCode 城市代码
     * @param weatherCode 天气代码（实时天气或者未来天气预报）
     * @return
     */
    Result findForecastWeatherToMysql(String weatherApiKey, String cityCode, String weatherCode);

    /**
     * 获取实时天气并保存到mysql中
     * @param weatherApiKey 天气apiKey
     * @param cityCode  城市代码
     * @param weatherCode 天气代码（实时天气或者未来天气预报）
     * @return
     */
    Result findLiveWeather(String weatherApiKey, String cityCode, String weatherCode);

    /**
     * 获取天气预报并保存到mysql中
     * @param weatherApiKey 天气apikey
     * @param cityCode 城市代码
     * @param weatherCode 天气代码（实时天气或者未来天气预报）
     * @return
     */
    Result findForecastWeather(String weatherApiKey, String cityCode, String weatherCode);
}
