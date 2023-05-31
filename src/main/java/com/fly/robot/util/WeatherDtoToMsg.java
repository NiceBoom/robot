package com.fly.robot.util;

import com.fly.robot.pojo.ForecastWeatherDTO;
import com.fly.robot.pojo.LiveWeatherDTO;

import java.util.List;

//把天气dto转化成消息信息
//TODO 代码有点冗余未来优化
public class WeatherDtoToMsg {

    /**
     * 发送默认城市天气预报返回的天气消息
     * @param forecastWeatherDto 预报天气dto
     * @return 组装好的天气消息
     */
    public static String conversionForecastWeatherDtoToMsg(ForecastWeatherDTO forecastWeatherDto){

        //格式化天气预报更新时间
        String time = forecastWeatherDto.getForecasts().get(0).getReporttime().substring(0, 4) + "年" + forecastWeatherDto.getForecasts().get(0).getReporttime().substring(5, 7) + "月" + forecastWeatherDto.getForecasts().get(0).getReporttime().substring(8, 10) + "日" +
                forecastWeatherDto.getForecasts().get(0).getReporttime().substring(11, 13) + "时" + forecastWeatherDto.getForecasts().get(0).getReporttime().substring(14, 16) + "分";
        List<ForecastWeatherDTO.CityForecast.WeatherForecast> weatherCasts = forecastWeatherDto.getForecasts().get(0).getCasts();
        //拼接天气预报消息
        String forecastWeatherMsg =
                "自动发送天气预报默认的城市为：" + forecastWeatherDto.getForecasts().get(0).getCity() + "。此天气预报更新时间为" + time + "。下面是详细的天气情况:" +
                        weatherCasts.get(0).getDate().substring(0, 4) + "年" + weatherCasts.get(0).getDate().substring(5, 7) + "月" + weatherCasts.get(0).getDate().substring(8, 10) + "日" +
                        "的白天天气情况为" + weatherCasts.get(0).getDayweather() + "，室外气温为" + weatherCasts.get(0).getDaytemp() + "摄氏度，风向为" +
                        weatherCasts.get(0).getDaywind() + "风，风力" + weatherCasts.get(0).getDaypower() + "级。夜间天气为" + weatherCasts.get(0).getNightweather() + "，室外气温" +
                        weatherCasts.get(0).getNighttemp() + "摄氏度，风向为" + weatherCasts.get(0).getNightwind() + "风，风力" + weatherCasts.get(0).getNightpower() + "级。" +
                        weatherCasts.get(1).getDate().substring(0, 4) + "年" + weatherCasts.get(1).getDate().substring(5, 7) + "月" + weatherCasts.get(1).getDate().substring(8, 10) + "日" +
                        "的白天天气情况为" + weatherCasts.get(1).getDayweather() + "，室外气温为" + weatherCasts.get(1).getDaytemp() + "摄氏度，风向为" +
                        weatherCasts.get(1).getDaywind() + "风，风力" + weatherCasts.get(1).getDaypower() + "级。夜间天气为" + weatherCasts.get(1).getNightweather() + "，室外气温" +
                        weatherCasts.get(1).getNighttemp() + "摄氏度，风向为" + weatherCasts.get(1).getNightwind() + "风，风力" + weatherCasts.get(1).getNightpower() + "级。" +
                        weatherCasts.get(2).getDate().substring(0, 4) + "年" + weatherCasts.get(2).getDate().substring(5, 7) + "月" + weatherCasts.get(2).getDate().substring(8, 10) + "日" +
                        "的白天天气情况为" + weatherCasts.get(2).getDayweather() + "，室外气温为" + weatherCasts.get(2).getDaytemp() + "摄氏度，风向为" +
                        weatherCasts.get(2).getDaywind() + "风，风力" + weatherCasts.get(2).getDaypower() + "级。夜间天气为" + weatherCasts.get(2).getNightweather() + "，室外气温" +
                        weatherCasts.get(2).getNighttemp() + "摄氏度，风向为" + weatherCasts.get(2).getNightwind() + "风，风力" + weatherCasts.get(2).getNightpower() + "级。" +
                        weatherCasts.get(3).getDate().substring(0, 4) + "年" + weatherCasts.get(3).getDate().substring(5, 7) + "月" + weatherCasts.get(3).getDate().substring(8, 10) + "日" +
                        "的白天天气情况为" + weatherCasts.get(3).getDayweather() + "，室外气温为" + weatherCasts.get(3).getDaytemp() + "摄氏度，风向为" +
                        weatherCasts.get(3).getDaywind() + "风，风力" + weatherCasts.get(3).getDaypower() + "级。夜间天气为" + weatherCasts.get(3).getNightweather() + "，室外气温" +
                        weatherCasts.get(3).getNighttemp() + "摄氏度，风向为" + weatherCasts.get(3).getNightwind() + "风，风力" + weatherCasts.get(3).getNightpower() + "级。";
        return forecastWeatherMsg;
    }

    /**
     *     查询未来天气返回的天气消息
     * @param forecastWeatherDto 未来天气预报dto
     * @return 组装好的天气消息
     */
    public static String conversionSearchForecastWeatherDtoToMsg(ForecastWeatherDTO forecastWeatherDto){

        //格式化天气预报更新时间
        String time = forecastWeatherDto.getForecasts().get(0).getReporttime().substring(0, 4) + "年" + forecastWeatherDto.getForecasts().get(0).getReporttime().substring(5, 7) + "月" + forecastWeatherDto.getForecasts().get(0).getReporttime().substring(8, 10) + "日" +
                forecastWeatherDto.getForecasts().get(0).getReporttime().substring(11, 13) + "时" + forecastWeatherDto.getForecasts().get(0).getReporttime().substring(14, 16) + "分";
        List<ForecastWeatherDTO.CityForecast.WeatherForecast> weatherCasts = forecastWeatherDto.getForecasts().get(0).getCasts();
        //拼接天气预报消息
        String forecastWeatherMsg =
                "您查询的城市为：" + forecastWeatherDto.getForecasts().get(0).getCity() + "。此天气预报更新时间为" + time + "。下面是详细的天气情况:" +
                        weatherCasts.get(0).getDate().substring(0, 4) + "年" + weatherCasts.get(0).getDate().substring(5, 7) + "月" + weatherCasts.get(0).getDate().substring(8, 10) + "日" +
                        "的白天天气情况为" + weatherCasts.get(0).getDayweather() + "，室外气温为" + weatherCasts.get(0).getDaytemp() + "摄氏度，风向为" +
                        weatherCasts.get(0).getDaywind() + "风，风力" + weatherCasts.get(0).getDaypower() + "级。夜间天气为" + weatherCasts.get(0).getNightweather() + "，室外气温" +
                        weatherCasts.get(0).getNighttemp() + "摄氏度，风向为" + weatherCasts.get(0).getNightwind() + "风，风力" + weatherCasts.get(0).getNightpower() + "级。" +
                        weatherCasts.get(1).getDate().substring(0, 4) + "年" + weatherCasts.get(1).getDate().substring(5, 7) + "月" + weatherCasts.get(1).getDate().substring(8, 10) + "日" +
                        "的白天天气情况为" + weatherCasts.get(1).getDayweather() + "，室外气温为" + weatherCasts.get(1).getDaytemp() + "摄氏度，风向为" +
                        weatherCasts.get(1).getDaywind() + "风，风力" + weatherCasts.get(1).getDaypower() + "级。夜间天气为" + weatherCasts.get(1).getNightweather() + "，室外气温" +
                        weatherCasts.get(1).getNighttemp() + "摄氏度，风向为" + weatherCasts.get(1).getNightwind() + "风，风力" + weatherCasts.get(1).getNightpower() + "级。" +
                        weatherCasts.get(2).getDate().substring(0, 4) + "年" + weatherCasts.get(2).getDate().substring(5, 7) + "月" + weatherCasts.get(2).getDate().substring(8, 10) + "日" +
                        "的白天天气情况为" + weatherCasts.get(2).getDayweather() + "，室外气温为" + weatherCasts.get(2).getDaytemp() + "摄氏度，风向为" +
                        weatherCasts.get(2).getDaywind() + "风，风力" + weatherCasts.get(2).getDaypower() + "级。夜间天气为" + weatherCasts.get(2).getNightweather() + "，室外气温" +
                        weatherCasts.get(2).getNighttemp() + "摄氏度，风向为" + weatherCasts.get(2).getNightwind() + "风，风力" + weatherCasts.get(2).getNightpower() + "级。" +
                        weatherCasts.get(3).getDate().substring(0, 4) + "年" + weatherCasts.get(3).getDate().substring(5, 7) + "月" + weatherCasts.get(3).getDate().substring(8, 10) + "日" +
                        "的白天天气情况为" + weatherCasts.get(3).getDayweather() + "，室外气温为" + weatherCasts.get(3).getDaytemp() + "摄氏度，风向为" +
                        weatherCasts.get(3).getDaywind() + "风，风力" + weatherCasts.get(3).getDaypower() + "级。夜间天气为" + weatherCasts.get(3).getNightweather() + "，室外气温" +
                        weatherCasts.get(3).getNighttemp() + "摄氏度，风向为" + weatherCasts.get(3).getNightwind() + "风，风力" + weatherCasts.get(3).getNightpower() + "级。";
        return forecastWeatherMsg;
    }
    /**
     *     发送默认城市实时天气返回的天气消息
     * @param liveWeatherDto 实时天气预报dto
     * @return 组装好的天气消息
     */
    public static String conversionLiveWeatherDtoToMsg(LiveWeatherDTO liveWeatherDto){

        //时间截取转换格式
        String time = "年" + liveWeatherDto.getLives().get(0).getReporttime().substring(5, 7) + "月" + liveWeatherDto.getLives().get(0).getReporttime().substring(8, 10) + "日" + liveWeatherDto.getLives().get(0).getReporttime().substring(11, 13) +
                "时" + liveWeatherDto.getLives().get(0).getReporttime().substring(14, 16) + "分";

        //拼接天气消息
        String liveWeatherMsg = "自动发送实时天气默认的城市为：" + liveWeatherDto.getLives().get(0).getCity() + "，当前天气：" + liveWeatherDto.getLives().get(0).getWeather() + "，实时气温为" + liveWeatherDto.getLives().get(0).getTemperature() + "摄氏度，空气湿度为" +
                liveWeatherDto.getLives().get(0).getHumidity() + "%，" + liveWeatherDto.getLives().get(0).getWinddirection() + "风" + liveWeatherDto.getLives().get(0).getWindpower() + "级。更新时间为" + time + "。";
        return liveWeatherMsg;
    }

    /**
     *  查询实时天气返回的天气消息
     * @param liveWeatherDto 实时天气DTO
     * @return 组装好的天气消息
     */
    public static String conversionSearchLiveWeatherDtoToMsg(LiveWeatherDTO liveWeatherDto){

        //时间截取转换格式
        String time = "年" + liveWeatherDto.getLives().get(0).getReporttime().substring(5, 7) + "月" + liveWeatherDto.getLives().get(0).getReporttime().substring(8, 10) + "日" + liveWeatherDto.getLives().get(0).getReporttime().substring(11, 13) +
                "时" + liveWeatherDto.getLives().get(0).getReporttime().substring(14, 16) + "分";

        //拼接天气消息
        String liveWeatherMsg = "您当前所在的城市为：" + liveWeatherDto.getLives().get(0).getCity() + "，当前天气：" + liveWeatherDto.getLives().get(0).getWeather() + "，实时气温为" + liveWeatherDto.getLives().get(0).getTemperature() + "摄氏度，空气湿度为" +
                liveWeatherDto.getLives().get(0).getHumidity() + "%，" + liveWeatherDto.getLives().get(0).getWinddirection() + "风" + liveWeatherDto.getLives().get(0).getWindpower() + "级。更新时间为" + time + "。";
        return liveWeatherMsg;
    }

}
