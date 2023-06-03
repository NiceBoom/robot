package com.fly.robot.util;

import com.fly.robot.pojo.FlyBookConfig;

public class MsgVerification {
    /**
     * @param queryWeatherMsg 接收到的消息
     * @return 天气类型代码
     */
    public static String queryWeatherMsgVerification(String queryWeatherMsg) {
        String[] queryWeatherMsgLength = queryWeatherMsg.split(",|，");
        if (queryWeatherMsgLength.length == 3 && "天气".equals(queryWeatherMsgLength[2])) {
            if ("实时".equals(queryWeatherMsgLength[1]))
                return FlyBookConfig.SEARCH_LIVE_WEATHER_MSG_CODE;
            if ("未来".equals(queryWeatherMsgLength[1]))
                return FlyBookConfig.SEARCH_FORECAST_WEATHER_MSG_CODE;
            return null;
        }
        return null;
    }
}
