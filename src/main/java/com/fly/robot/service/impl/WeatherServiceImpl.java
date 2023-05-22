package com.fly.robot.service.impl;

import com.fly.robot.entity.ApiCode;
import com.fly.robot.service.WeatherService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WeatherServiceImpl implements WeatherService {
    /**
     * 获取实时天气
     * @return 实时天气JSON
     */
    @Override
    public String findLiveWeather() {
        String liveWeather =
                sendRequestGetWeather(ApiCode.WEATHER_API_KEY, ApiCode.WEATHER_API_CITY_CODE, ApiCode.GET_LIVE_WEATHER);

        return liveWeather;
    }

    /**
     * 获取天气预报
     * @return 天气预报JSON
     */
    @Override
    public String findForecastWeather() {

        String forecastWeather =
                sendRequestGetWeather(ApiCode.WEATHER_API_KEY, ApiCode.WEATHER_API_CITY_CODE, ApiCode.GET_FORECAST_WEATHER);
        return forecastWeather;
    }

    private String sendRequestGetWeather(String APIkey, String cityCode, String extensions) {

        //拼接url链接与参数
        String requestUrlString =
                ApiCode.WEATHER_API_URL + "?key=" + APIkey + "&city=" + cityCode + "&extensions=" + extensions;
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


//    public class GetRequestExample {
//        public static void main(String[] args) {
//            try {
//                // 拼接URL和参数
//                String requestUrl = urlString + "?param1=" + encodedParam1 + "&param2=" + encodedParam2 + "&param3=" + encodedParam3;
//
//                // 创建URL对象
//                URL url = new URL(requestUrl);
//
//                // 打开连接
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//
//                // 发送请求并获取响应
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line;
//                StringBuilder response = new StringBuilder();
//                while ((line = reader.readLine()) != null) {
//                    response.append(line);
//                }
//                reader.close();
//
//                // 输出响应结果
//                System.out.println(response.toString());
//
//                // 关闭连接
//                connection.disconnect();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
