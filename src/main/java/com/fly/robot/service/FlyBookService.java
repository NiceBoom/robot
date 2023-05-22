package com.fly.robot.service;

public interface FlyBookService {
    /**
     * 发送实时天气数据
     * @return 发送消息返回的数据
     */
    String sendLiveWeatherMsg();

    /**
     * 发送未来三天天气预报
     * @return 发送消息返回的数据
     */
    String sendForecastWeatherMsg();
}
