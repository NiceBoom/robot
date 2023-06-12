package com.fly.robot.controller;

import com.fly.robot.dao.WeatherRepository;
import com.fly.robot.pojo.GaodeCode;
import com.fly.robot.dto.WeatherDTO;
import com.fly.robot.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/weather")
@EnableScheduling
public class WeatherController {
    private final WeatherService weatherService;
    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherController(WeatherService weatherService,
                             WeatherRepository weatherRepository) {
        this.weatherService = weatherService;
        this.weatherRepository = weatherRepository;
    }

    //从高德API查询实时天气并保存到mysql中
    //每天早八点半，每隔一小时获取一次实时天气，一直到晚上21.30
    //也可以手动获取实时天气
    @Scheduled(cron = "0 30 8,9,10,11,12,13,14,15,16,17,18,19,20,21 * * ? ")
    @GetMapping("/findLiveWeatherSaveToMysql")
    public WeatherDTO findLiveWeatherSaveToMysql() throws Exception {
        return weatherService.getWeather(GaodeCode.BEIJING_CITY_ADCODE, GaodeCode.GET_LIVE_WEATHER_CODE);
    }

    //从高德API查询天气预报并保存到mysql中
    //每天9点、10点、12点、13点、19点、20点获取天气预报保存到mysql中
    //也可以手动获取天气预报
    @Scheduled(cron = "0 0 9,10,12,13,19,20 * * ? ")
    @GetMapping("/findForecastWeatherToMysql")
    public WeatherDTO findForecastWeatherToMysql() throws Exception {
        return weatherService.getWeather(GaodeCode.BEIJING_CITY_ADCODE, GaodeCode.GET_FORECAST_WEATHER_CODE);
    }

        //TODO 在redis添加过期码，adcode、对应的城市名称、过期时间，人工查询天气先到redis或者mysql中查询，过期再查询新的天气情况。(可以优化加入es)
        //TODO 查询流程，人工查询天气，启动服务器先把mysql中的adcode缓存到redis，查询时候先从redis获取adcode代码，
        //TODO redis没有就去高德查询，查询结果缓存到mysql与redis中
        //TODO 优化从第三方获取数据判断获取的结果是否为空的过程，以及返回错误代码判断的过程
        //TODO 优化对参数数据的校验，加入日志系统，添加每天查询第三方接口次数限制，记录每天请求接口次数，凌晨更新
        //TODO 优化数据库查询时间不对问题，linux与windows系统时间兼容问题，mysql中时间对不上
}
