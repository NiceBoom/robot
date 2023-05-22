package com.fly.robot.service.impl;

import com.fly.robot.service.WeatherService;

public class WeatherServiceImpl implements WeatherService {
    @Override
    public String findLiveWeather() {
        return "这个是实时天气";
    }
}
