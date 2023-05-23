package com.fly.robot.controller;

import com.fly.robot.dao.TableForecastWeatherRepository;
import com.fly.robot.dao.TableLiveWeatherRepository;
import com.fly.robot.pojo.TableForecastWeather;
import com.fly.robot.pojo.TableLiveWeather;
import com.fly.robot.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Value("${feishu.appid}")
    private String feishuAppId; //从配置文件读取feishu.appid

    @Value("${feishu.app-secret}")
    private String feishuAppSecret;//从配置文件读取飞书secret

    @Value("${feishu.robot-webhook-address}")
    private String robotWebHookAddress;//读取飞书robot web hook 地址

    @Value("${gaode.weather-api-key}")
    private String weatherApiKey; //读取高德ApiKey

    @Value("${gaode.weather-api-url}")
    private String weatherApiUrl; //读取高德api url地址

    @Value("${gaode.weather-api-city-code}")
    private String cityCode;//城市代码

    @Value("${gaode.get-live-weather-code}")
    private String liveWeatherCode;//实时天气代码

    @Value("${gaode.get-forecast-weather-code}")
    private String forecastWeatherCode;//天气预报代码


    //从高德API查询实时天气并保存到mysql中
    //每天早八点半，每隔一小时获取一次实时天气，一直到晚上21.30
    //也可以手动获取实时天气
    @Scheduled(cron = "0 30 8,9,10,11,12,13,14,15,16,17,18,19,20,21 * * ? *")
    @GetMapping("/findLiveWeatherSaveToMysql")
    public String findLiveWeatherSaveToMysql() {
        return weatherService.findLiveWeatherSaveToMysql(weatherApiKey, cityCode, liveWeatherCode);
    }

    //从高德API查询天气预报并保存到mysql中
    //每天9点、10点、12点、13点、19点、20点获取天气预报保存到mysql中
    //也可以手动获取天气预报
    @Scheduled(cron = "0 0 9,10,12,13,19,20 * * ? *")
    @GetMapping("/findForecastWeatherToMysql")
    public String findForecastWeatherToMysql() {
        return weatherService.findForecastWeatherToMysql(weatherApiKey, cityCode, forecastWeatherCode);

    }

    /**
     * 从高德API获取实时天气情况
     *
     * @return 天气JSON
     */
    @GetMapping("/findLiveWeather")
    public String findLiveWeather() {

        String liveWeather = weatherService.findLiveWeather(weatherApiKey, cityCode, liveWeatherCode);

        return liveWeather;
    }

    /**
     * 从高德API获取未来天气情况
     *
     * @return 天气JSON
     */
    @GetMapping("/findForecastWeather")
    public String findForecastWeather() {

        String forecastWeather = weatherService.findForecastWeather(weatherApiKey, cityCode, forecastWeatherCode);

        return forecastWeather;
    }

    private final TableLiveWeatherRepository liveWeatherRepository;
    private final TableForecastWeatherRepository forecastWeatherRepository;

    @Autowired
    public WeatherController(TableLiveWeatherRepository liveWeatherRepository, TableForecastWeatherRepository forecastWeatherRepository) {
        this.liveWeatherRepository = liveWeatherRepository;
        this.forecastWeatherRepository = forecastWeatherRepository;
    }

    //注入查找实时天气表格
    @GetMapping("/getLiveWeather")
    public List<TableLiveWeather> getLiveWeather() {
        return liveWeatherRepository.findAll();
    }

    //注入查找天气预报表格
    @GetMapping("/getForecastWeather")
    public List<TableForecastWeather> getForecastWeather() {
        return forecastWeatherRepository.findAll();
    }

}
