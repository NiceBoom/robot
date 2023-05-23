package com.fly.robot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.dao.TableForecastWeatherRepository;
import com.fly.robot.dao.TableLiveWeatherRepository;
import com.fly.robot.pojo.*;
import com.fly.robot.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    public TableLiveWeatherRepository tableLiveWeatherRepository;

    @Autowired
    public TableForecastWeatherRepository tableForecastWeatherRepository;

    @Value("${gaode.weather-api-url}")
    private String weatherApiUrl; //读取高德api url地址

    /**
     * 获取实时天气
     * 准备优化掉
     *
     * @return 实时天气JSON
     */
    @Override
    public String findLiveWeather(String weatherApiKey, String cityCode, String weatherCode) {
        String liveWeather =
                sendRequestGetWeather(weatherApiKey, cityCode, weatherCode);
        return liveWeather;
    }

    /**
     * 获取实时天气并保存到mysql中
     *
     * @param weatherApiKey 天气apiKey
     * @param cityCode      城市代码
     * @param weatherCode   天气代码（实时天气或者未来天气预报）
     * @return
     */
    @Override
    public String findLiveWeatherSaveToMysql(String weatherApiKey, String cityCode, String weatherCode) {
        //获取实时天气
        String liveWeather =
                sendRequestGetWeather(weatherApiKey, cityCode, weatherCode);
        try {
            //格式化实时天气
            ObjectMapper mapper = new ObjectMapper();
            LiveWeatherDto liveWeatherDto = mapper.readValue(liveWeather, LiveWeatherDto.class);
            Lives lives = liveWeatherDto.getLives().get(0);
            //组装mysql格式的实时天气
            TableLiveWeather tableLiveWeather = new TableLiveWeather();
            tableLiveWeather.setCityId(lives.getAdcode());
            tableLiveWeather.setCityName(lives.getCity());
            tableLiveWeather.setLiveWeather(liveWeather);
            tableLiveWeather.setCreateAt(LocalDateTime.now());
            //保存实时天气到mysql中
            TableLiveWeather save = tableLiveWeatherRepository.save(tableLiveWeather);
            System.out.println(save);
            return save.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "save live weather fail";

    }

    /**
     * 获取天气预报并保存到mysql中
     *
     * @return
     */
    @Override
    public String findForecastWeatherToMysql(String weatherApiKey, String cityCode, String weatherCode) {
        try {
            //获取天气预报
            String forecastWeather =
                    sendRequestGetWeather(weatherApiKey, cityCode, weatherCode);
            //格式化天气预报数据，转换成DTO
            ObjectMapper mapper = new ObjectMapper();
            ForecastWeatherDto forecastWeatherDto = mapper.readValue(forecastWeather, ForecastWeatherDto.class);
            List<Forecasts> forecasts = forecastWeatherDto.getForecasts();
            List<Casts> weatherCasts = forecasts.get(0).getCasts();
            //组装mysql格式的天气预报数据
            TableForecastWeather tableForecastWeather = new TableForecastWeather();
            tableForecastWeather.setCityId(forecasts.get(0).getAdcode());
            tableForecastWeather.setCityName(forecasts.get(0).getCity());
            tableForecastWeather.setForecastWeather(forecastWeather);
            tableForecastWeather.setCreateAt(LocalDateTime.now());
            //保存天气预报到mysql中
            TableForecastWeather save = tableForecastWeatherRepository.save(tableForecastWeather);
            return save.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "save forecast Weather fail";
    }

    /**
     * 获取天气预报
     *
     * @return 天气预报JSON
     */
    @Override
    public String findForecastWeather(String weatherApiKey, String cityCode, String weatherCode) {

        String forecastWeather =
                sendRequestGetWeather(weatherApiKey, cityCode, weatherCode);
        return forecastWeather;
    }

    /**
     * 发送请求获取天气
     *
     * @param APIkey     请求地址链接
     * @param cityCode   城市代码
     * @param extensions 实时天气或者天气预报代码
     * @return
     */
    private String sendRequestGetWeather(String APIkey, String cityCode, String extensions) {

        //拼接url链接与参数
        String requestUrlString =
                weatherApiUrl + "?key=" + APIkey + "&city=" + cityCode + "&extensions=" + extensions;
        try {
            //创建URL对象
            URL weatherUrl = new URL(requestUrlString);
            //打开链接
            HttpURLConnection connection = (HttpURLConnection) weatherUrl.openConnection();
            connection.setRequestMethod("GET");
            //发送请求并获得响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 输出响应结果
            System.out.println(response.toString());

            // 关闭连接
            connection.disconnect();

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "get Weather fail";
    }


}
