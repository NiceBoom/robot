package com.fly.robot.controller;

import com.fly.robot.dao.WeatherRepository;
import com.fly.robot.pojo.Weather;
import com.fly.robot.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * 获取未来天气情况
     * @return 天气JSON
     */
    @GetMapping("/findForecastWeather")
    public String findForecastWeather(){

        String forecastWeather = weatherService.findForecastWeather();

        return forecastWeather;
    }

    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherController(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @GetMapping("/weathers")
    public List<Weather> getWeathers() {
        return weatherRepository.findAll();
    }

}
