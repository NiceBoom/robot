package com.fly.robot.pojo;

/**
 * 高德配置常量数据
 */
public class GaodeConfig {

    //请求URL
    //获取地理编码API服务地址
    public static final String GET_ADDRESS_ADCODE_URL = "https://restapi.amap.com/v3/geocode/geo";
    //获取天气信息查询API服务地址
    public static final String GET_WEATHER_API_URL = "https://restapi.amap.com/v3/weather/weatherInfo";

    //请求代码
    //获取实时天气预报的代码
    public static final String GET_LIVE_WEATHER_CODE = "base";
    //获取未来天气预报的代码
    public static final String GET_FORECAST_WEATHER_CODE = "all";
    //默认获取天气信息的城市ID 北京市
    public static final String BEIJING_CITY_ADCODE = "110000";
}
