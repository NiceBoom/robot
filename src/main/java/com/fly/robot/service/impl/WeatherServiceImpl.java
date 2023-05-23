package com.fly.robot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.dao.TableForecastWeatherRepository;
import com.fly.robot.dao.TableLiveWeatherRepository;
import com.fly.robot.entity.Result;
import com.fly.robot.entity.StatusCode;
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
     * @return 实时天气JSON
     */
    @Override
    public Result findLiveWeather(String weatherApiKey, String cityCode, String weatherCode) {

        return sendRequestGetWeather(weatherApiKey, cityCode, weatherCode);
    }

    /**
     * 获取实时天气并保存到mysql中
     * @param weatherApiKey 天气apiKey
     * @param cityCode      城市代码
     * @param weatherCode   天气代码（实时天气或者未来天气预报）
     * @return
     */
    @Override
    public Result findLiveWeatherSaveToMysql(String weatherApiKey, String cityCode, String weatherCode) {
        //获取实时天气
        Result liveWeatherResult =
                sendRequestGetWeather(weatherApiKey, cityCode, weatherCode);
        String liveWeather = (String) liveWeatherResult.getData();
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
            tableLiveWeatherRepository.save(tableLiveWeather);

            return new Result();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //保存天气预报失败代码
        Result<Object> failResult = new Result<>();
        failResult.setFlag(false);
        failResult.setMessage("save forecast weather fail");
        failResult.setCode(StatusCode.ERROR);

        return failResult;

    }

    /**
     * 获取天气预报
     * @return 天气预报JSON
     */
    @Override
    public Result findForecastWeather(String weatherApiKey, String cityCode, String weatherCode) {

        return sendRequestGetWeather(weatherApiKey, cityCode, weatherCode);

    }

    /**
     * 获取天气预报并保存到mysql中
     *
     * @param weatherApiKey 天气apiKey
     * @param cityCode      城市代码
     * @param weatherCode   天气代码（实时天气或者未来天气预报）
     * @return
     */
    @Override
    public Result findForecastWeatherToMysql(String weatherApiKey, String cityCode, String weatherCode) {
        try {
            //获取天气预报
            Result forecastWeatherResult =
                    sendRequestGetWeather(weatherApiKey, cityCode, weatherCode);
            String forecastWeather = (String) forecastWeatherResult.getData();
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
                tableForecastWeatherRepository.save(tableForecastWeather);

                return new Result();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //保存天气预报失败代码
        Result<Object> failResult = new Result<>();
        failResult.setFlag(false);
        failResult.setMessage("save live weather fail");
        failResult.setCode(StatusCode.ERROR);
        return failResult;
    }

    /**
     * 发送请求获取天气
     * @param APIkey     请求地址链接
     * @param cityCode   城市代码
     * @param extensions 实时天气或者天气预报代码
     * @return
     */
    private Result sendRequestGetWeather(String APIkey, String cityCode, String extensions) {

        //拼接url链接与参数
        String requestUrlString =
                weatherApiUrl + "?key=" + APIkey + "&city=" + cityCode + "&extensions=" + extensions;
        HttpURLConnection connection = null;
        try {
            //创建URL对象
            URL weatherUrl = new URL(requestUrlString);
            //打开链接
            connection = (HttpURLConnection) weatherUrl.openConnection();
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

            Result<Object> getWeatherResult = new Result<>();
            getWeatherResult.setData(response.toString());
            return getWeatherResult;

        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取天气预报失败代码
        Result<Object> failResult = new Result<>();
        failResult.setFlag(false);
        failResult.setMessage("save live weather fail");
        failResult.setCode(StatusCode.ERROR);
        return failResult;
    }


}
