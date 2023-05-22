package com.fly.robot.service;

public interface WeatherService {

    /**
     * 查询实时天气
     * @return 实时天气数据
     */
    String findLiveWeather();

    /**
     *  查询未来三天天气
     * @return 天气预报
     */
    String findForecastWeather();
}
