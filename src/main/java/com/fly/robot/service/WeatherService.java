package com.fly.robot.service;

public interface WeatherService {

    /**
     * 获取实时天气并保存到mysql中
     * @param weatherApiKey 天气apiKey
     * @param cityCode  城市代码
     * @param weatherCode 天气代码（实时天气或者未来天气预报）
     * @return
     */
    String findLiveWeatherSaveToMysql(String weatherApiKey, String cityCode, String weatherCode);

    /**
     * 获取天气预报并保存到mysql中
     * @param weatherApiKey 天气apikey
     * @param cityCode 城市代码
     * @param weatherCode 天气代码（实时天气或者未来天气预报）
     * @return
     */
    String findForecastWeatherToMysql(String weatherApiKey, String cityCode, String weatherCode);

    /**
     * 获取实时天气并保存到mysql中
     * @param weatherApiKey 天气apiKey
     * @param cityCode  城市代码
     * @param weatherCode 天气代码（实时天气或者未来天气预报）
     * @return
     */
    String findLiveWeather(String weatherApiKey, String cityCode, String weatherCode);

    /**
     * 获取天气预报并保存到mysql中
     * @param weatherApiKey 天气apikey
     * @param cityCode 城市代码
     * @param weatherCode 天气代码（实时天气或者未来天气预报）
     * @return
     */
    String findForecastWeather(String weatherApiKey, String cityCode, String weatherCode);
}
