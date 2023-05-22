package com.fly.robot.controller;

import com.fly.robot.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * 获取实时天气情况
     * @return 天气JSON
     */
    @GetMapping("/findLiveWeather")
    public String findLiveWeather(){

        String liveWeather = weatherService.findLiveWeather();

        return liveWeather;
    }

    @GetMapping("/findForecastWeather")
    public String findForecastWeather(){

        String forecastWeather = weatherService.findForecastWeather();

        return forecastWeather;
    }

}
