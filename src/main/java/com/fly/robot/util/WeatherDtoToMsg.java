package com.fly.robot.util;

import com.fly.robot.pojo.FlyBookConfig;

import com.fly.robot.dto.WeatherDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//把天气dto转化成消息信息
//TODO 代码有点冗余未来优化
public class WeatherDtoToMsg {
    public static String conversionWeatherDtoToMsg(WeatherDTO weatherDTO, String sendMsgCode) {

        //自动发送实时天气返回消息
        if (FlyBookConfig.SEND_LIVE_WEATHER_MSG_CODE.equals(sendMsgCode)) {
            WeatherDTO.Live liveWeatherInfo = weatherDTO.getLives().get(0);
            String weatherReportTime =
                    LocalDateTime.parse(weatherDTO.getLives().get(0).getReporttime(),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            .format(DateTimeFormatter.ofPattern("yyyy年M月d日H时m分"));
            return "自动发送实时天气默认的城市为：" + liveWeatherInfo.getCity() + "，当前天气：" + liveWeatherInfo.getWeather() +
                    "，实时气温为" + liveWeatherInfo.getTemperature() + "摄氏度，空气湿度为" + liveWeatherInfo.getHumidity() +
                    "%，" + liveWeatherInfo.getWinddirection() + "风" + liveWeatherInfo.getWindpower() + "级。更新时间为" + weatherReportTime + "。";
        }
        //查找发送实时天气返回消息
        if (FlyBookConfig.SEARCH_LIVE_WEATHER_MSG_CODE.equals(sendMsgCode)) {
            WeatherDTO.Live liveWeatherInfo = weatherDTO.getLives().get(0);
            String weatherReportTime =
                    LocalDateTime.parse(weatherDTO.getLives().get(0).getReporttime(),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            .format(DateTimeFormatter.ofPattern("yyyy年M月d日H时m分"));
            return "您所查询的城市为：" + liveWeatherInfo.getCity() + "，当前天气：" + liveWeatherInfo.getWeather() +
                    "，实时气温为" + liveWeatherInfo.getTemperature() + "摄氏度，空气湿度为" + liveWeatherInfo.getHumidity() +
                    "%，" + liveWeatherInfo.getWinddirection() + "风" + liveWeatherInfo.getWindpower() + "级。更新时间为" + weatherReportTime + "。";
        }
        //自动发送未来天气预报返回消息
        if (FlyBookConfig.SEND_FORECAST_WEATHER_MSG_CODE.equals(sendMsgCode)) {
            ArrayList<WeatherDTO.Forecast.Cast> forecastWeatherCasts = weatherDTO.getForecasts().get(0).getCasts();
            String weatherReportTime =
                    LocalDateTime.parse(weatherDTO.getForecasts().get(0).getReporttime(),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            .format(DateTimeFormatter.ofPattern("yyyy年M月d日H时m分"));
            StringBuilder forecastWeatherMsg =
                    new StringBuilder("自动发送天气预报默认的城市为：")
                            .append(weatherDTO.getForecasts().get(0).getCity())
                            .append("。此天气预报更新时间为")
                            .append(weatherReportTime)
                            .append("。下面是详细的天气情况:");
            for (WeatherDTO.Forecast.Cast cast : forecastWeatherCasts) {
                forecastWeatherMsg.append(LocalDate.parse(cast.getDate()).format(DateTimeFormatter.ofPattern("yyyy年M月d日")))
                        .append("的白天天气情况为")
                        .append(cast.getDayweather())
                        .append("，室外气温为")
                        .append(cast.getDaytemp())
                        .append("摄氏度，风向为")
                        .append(cast.getDaywind())
                        .append("风，风力")
                        .append(cast.getDaypower())
                        .append("级。夜间天气为")
                        .append(cast.getNightweather())
                        .append("，室外气温")
                        .append(cast.getNighttemp())
                        .append("摄氏度，风向为")
                        .append(cast.getNightwind())
                        .append("风，风力")
                        .append(cast.getNightpower())
                        .append("级。");
            }
            return forecastWeatherMsg.toString();
        }
        //查找未来天气预报返回消息
        if (FlyBookConfig.SEARCH_FORECAST_WEATHER_MSG_CODE.equals(sendMsgCode)) {
            ArrayList<WeatherDTO.Forecast.Cast> forecastWeatherCasts = weatherDTO.getForecasts().get(0).getCasts();
            String weatherReportTime =
                    LocalDateTime.parse(weatherDTO.getForecasts().get(0).getReporttime(),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            .format(DateTimeFormatter.ofPattern("yyyy年M月d日H时m分"));
            StringBuilder searchForecastWeatherMsg =
                    new StringBuilder("您所查询的城市为：")
                            .append(weatherDTO.getForecasts().get(0).getCity())
                            .append("。此天气预报更新时间为")
                            .append(weatherReportTime)
                            .append("。下面是详细的天气情况:");
            for (WeatherDTO.Forecast.Cast cast : forecastWeatherCasts) {
                searchForecastWeatherMsg.append(LocalDate.parse(cast.getDate()).format(DateTimeFormatter.ofPattern("yyyy年M月d日")))
                        .append("的白天天气情况为")
                        .append(cast.getDayweather())
                        .append("，室外气温为")
                        .append(cast.getDaytemp())
                        .append("摄氏度，风向为")
                        .append(cast.getDaywind())
                        .append("风，风力")
                        .append(cast.getDaypower())
                        .append("级。夜间天气为")
                        .append(cast.getNightweather())
                        .append("，室外气温")
                        .append(cast.getNighttemp())
                        .append("摄氏度，风向为")
                        .append(cast.getNightwind())
                        .append("风，风力")
                        .append(cast.getNightpower())
                        .append("级。");

            }
            return searchForecastWeatherMsg.toString();
        }
        return null;
    }

}
