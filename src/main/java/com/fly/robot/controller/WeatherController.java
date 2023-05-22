package com.fly.robot.controller;

import com.fly.robot.dao.TableForecastWeatherRepository;
import com.fly.robot.dao.TableLiveWeatherRepository;
import com.fly.robot.dao.WeatherRepository;
import com.fly.robot.pojo.TableForecastWeather;
import com.fly.robot.pojo.TableLiveWeather;
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
     * 从高德API获取实时天气情况
     * @return 天气JSON
     */
    @GetMapping("/findLiveWeather")
    public String findLiveWeather(){

        String liveWeather = weatherService.findLiveWeather();

        return liveWeather;
    }

    /**
     * 从高德API获取未来天气情况
     * @return 天气JSON
     */
    @GetMapping("/findForecastWeather")
    public String findForecastWeather(){

        String forecastWeather = weatherService.findForecastWeather();

        return forecastWeather;
    }

    //注入查找测试天气表格
    private final WeatherRepository weatherRepository;
    private final TableLiveWeatherRepository liveWeatherRepository;
    private final TableForecastWeatherRepository forecastWeatherRepository;

    @Autowired
    public WeatherController(WeatherRepository weatherRepository, TableLiveWeatherRepository liveWeatherRepository, TableForecastWeatherRepository forecastWeatherRepository) {
        this.weatherRepository = weatherRepository;
        this.liveWeatherRepository = liveWeatherRepository;
        this.forecastWeatherRepository = forecastWeatherRepository;
    }

    @GetMapping("/weathers")
    public List<Weather> getWeathers() {
        return weatherRepository.findAll();
    }

    //注入查找实时天气表格
    @GetMapping("/getLiveWeather")
    public List<TableLiveWeather> getLiveWeather(){
        return liveWeatherRepository.findAll();
    }

    //注入查找天气预报表格
    @GetMapping("/getForecastWeather")
    public List<TableForecastWeather> getForecastWeather(){
        return forecastWeatherRepository.findAll();
    }

}
