package com.fly.robot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.dao.TableForecastWeatherRepository;
import com.fly.robot.dao.TableLiveWeatherRepository;
import com.fly.robot.entity.Result;
import com.fly.robot.pojo.TableForecastWeather;
import com.fly.robot.pojo.TableLiveWeather;
import com.fly.robot.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/weather")
@EnableScheduling
public class WeatherController {

    @Autowired
    private WeatherService weatherService;
    private final TableLiveWeatherRepository liveWeatherRepository;
    private final TableForecastWeatherRepository forecastWeatherRepository;

    @Autowired
    public WeatherController(TableLiveWeatherRepository liveWeatherRepository, TableForecastWeatherRepository forecastWeatherRepository) {
        this.liveWeatherRepository = liveWeatherRepository;
        this.forecastWeatherRepository = forecastWeatherRepository;
    }

    @Value("${gaode.web-api-key}")
    private String webApiKey; //读取高德ApiKey

    @Value("${gaode.weather-api-city-code}")
    private String cityCode;//城市代码

    @Value("${gaode.get-live-weather-code}")
    private String liveWeatherCode;//实时天气代码

    @Value("${gaode.get-forecast-weather-code}")
    private String forecastWeatherCode;//天气预报代码

    @Value("${gaode.get-address-adcode-url}")
    private String getAddressAdcodeUrl;//获取城市信息地址


    //从高德API查询实时天气并保存到mysql中
    //每天早八点半，每隔一小时获取一次实时天气，一直到晚上21.30
    //也可以手动获取实时天气
    @Scheduled(cron = "0 30 8,9,10,11,12,13,14,15,16,17,18,19,20,21 * * ? ")
    @GetMapping("/findLiveWeatherSaveToMysql")
    public Result findLiveWeatherSaveToMysql() {
        return weatherService.findLiveWeatherSaveToMysql(webApiKey, cityCode, liveWeatherCode);
    }

    //从高德API查询天气预报并保存到mysql中
    //每天9点、10点、12点、13点、19点、20点获取天气预报保存到mysql中
    //也可以手动获取天气预报
    @Scheduled(cron = "0 0 9,10,12,13,19,20 * * ? ")
    @GetMapping("/findForecastWeatherToMysql")
    public Result findForecastWeatherToMysql() {

        return weatherService.findForecastWeatherToMysql(webApiKey, cityCode, forecastWeatherCode);

    }

    /**
     * 从高德API获取实时天气情况
     * @return 天气JSON
     */
    @GetMapping("/findLiveWeather")
    public Result findLiveWeather() {

        return weatherService.findLiveWeather(webApiKey, cityCode, liveWeatherCode);

    }

    /**
     * 从高德API获取未来天气情况
     * @return 天气JSON
     */
    @GetMapping("/findForecastWeather")
    public Result findForecastWeather() {

        return weatherService.findForecastWeather(webApiKey, cityCode, forecastWeatherCode);

    }

    //查找所有在mysql中的实时天气数据
    @GetMapping("/getAllLiveWeatherFromMysql")
    public Result getAllLiveWeatherFromMysql() {

        List<TableLiveWeather> allLiveWeatherFromMysql = liveWeatherRepository.findAll();
        Result<Object> getAllLiveWeatherResult = new Result<>();
        getAllLiveWeatherResult.setData(allLiveWeatherFromMysql);

        return getAllLiveWeatherResult;
    }

    //查找所有在mysql中的天气预报数据
    @GetMapping("/getAllForecastWeatherFormMysql")
    public Result getAllForecastWeatherFormMysql() {
        List<TableForecastWeather> allForecastWeatherFromMysql = forecastWeatherRepository.findAll();
        Result<Object> getAllForecastWeatherResult = new Result<>();
        getAllForecastWeatherResult.setData(allForecastWeatherFromMysql);
        return getAllForecastWeatherResult;
    }

    //查找具体地址信息
    @GetMapping("/getAddressInfoForMsg")
    public Result getAddressInfoForMsg(@RequestParam("address") String findAddressInfoMsg){
        return weatherService.findAddressInfoByMsg(getAddressAdcodeUrl, webApiKey, findAddressInfoMsg);
    }

    //获取任意地址的天气预报数据
    @GetMapping("/getForecastWeatherForAddress")
    public Result getForecastWeatherForAddress(@RequestParam("address") String findAddressInfoMsg){
        //获取任意地址的具体数据
        Result addressInfoByMsg = weatherService.findAddressInfoByMsg(getAddressAdcodeUrl, webApiKey, findAddressInfoMsg);
        //提取出该地址的adcode代码
        JSONObject addressInfoJson = (JSONObject) addressInfoByMsg.getData();
        List<Map<String, Object>> geocodes = (List<Map<String, Object>>)addressInfoJson.get("geocodes");
        String adcode = (String)geocodes.get(0).get("adcode");
        //从高德获取具体的天气预报
        return weatherService.findForecastWeather(webApiKey, adcode, forecastWeatherCode);
        //TODO 对获取adcode过程进行优化，先从mysql中获取代码，mysql中没有的话再去高德查询adcode并将其存到mysql中，再返回天气数据
        //TODO 在redis添加过期码，adcode、对应的城市名称、过期时间，人工查询天气先到redis或者mysql中查询，过期再查询新的天气情况。(可以优化加入es)
        //TODO 查询流程，人工查询天气，启动服务器先把mysql中的adcode缓存到redis，查询时候先从redis获取adcode代码，
        //TODO redis没有就去高德查询，查询结果缓存到mysql与redis中
        //TODO 优化从第三方获取数据判断获取的结果是否为空的过程，以及返回错误代码判断的过程
        //TODO 优化对参数数据的校验，加入日志系统

    }
}
