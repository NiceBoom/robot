package com.fly.robot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.dao.TableForecastWeatherRepository;
import com.fly.robot.dao.TableLiveWeatherRepository;
import com.fly.robot.entity.Result;
import com.fly.robot.entity.StatusCode;
import com.fly.robot.pojo.*;
import com.fly.robot.service.FlyBookService;
import com.fly.robot.util.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlyBookServiceImpl implements FlyBookService {

    @Autowired
    private TableLiveWeatherRepository tableLiveWeatherRepository;

    @Autowired
    private TableForecastWeatherRepository tableForecastWeatherRepository;

    /**
     * 发送实时天气数据消息
     *
     * @param robotWebHookAddress 消息机器人web hook地址
     * @return
     */
    @Override
    public Result sendLiveWeatherMsg(String robotWebHookAddress) {
        try {
            //从mysql获取最新一条实时天气数据
            TableLiveWeather liveWeatherFromMysql = tableLiveWeatherRepository.findFirstByOrderByCreateAtDesc();
            String liveWeather = liveWeatherFromMysql.getLiveWeather();

            //格式化实时天气数据，转换成DTO
            ObjectMapper mapper = new ObjectMapper();
            LiveWeatherDto liveWeatherDto = mapper.readValue(liveWeather, LiveWeatherDto.class);
            Lives lives = liveWeatherDto.getLives().get(0);

            //时间截取转换格式
            String time = lives.getReporttime().substring(0, 4) + "年" + lives.getReporttime().substring(5, 7) + "月" + lives.getReporttime().substring(8, 10) + "日" +
                    lives.getReporttime().substring(11, 13) + "时" + lives.getReporttime().substring(14, 16) + "分";

            //拼接天气消息
            String liveWeatherMsg = "您当前所在的城市为" + lives.getCity() + "，当前天气：" + lives.getWeather() + "，实时气温为" + lives.getTemperature() + "摄氏度，空气湿度为" + lives.getHumidity() + "%，" + lives.getWinddirection() + "风" + lives.getWindpower() + "级。更新时间为" + time + "。";

            return sendWeatherMsg(robotWebHookAddress, liveWeatherMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //发送实时天气失败代码
        Result<Object> failResult = new Result<>();
        failResult.setFlag(false);
        failResult.setMessage("send live weather msg fail");
        failResult.setCode(StatusCode.ERROR);
        return failResult;
    }

    /**
     * 发送未来天气预报消息
     *
     * @param robotWebHookAddress 消息机器人web hook地址
     * @return
     */
    @Override
    public Result sendForecastWeatherMsg(String robotWebHookAddress) {
        try {
            //从mysql查找最新的一条天气预报数据
            TableForecastWeather forecastWeatherFromMysql = tableForecastWeatherRepository.findFirstByOrderByCreateAtDesc();
            //获取天气预报信息
            String forecastWeather = forecastWeatherFromMysql.getForecastWeather();
            //格式化天气预报数据，转换成DTO
            ObjectMapper mapper = new ObjectMapper();
            ForecastWeatherDto forecastWeatherDto = mapper.readValue(forecastWeather, ForecastWeatherDto.class);
            List<Forecasts> forecasts = forecastWeatherDto.getForecasts();
            List<Casts> weatherCasts = forecasts.get(0).getCasts();
            //格式化天气预报更新时间
            String time = forecasts.get(0).getReporttime().substring(0, 4) + "年" + forecasts.get(0).getReporttime().substring(5, 7) + "月" + forecasts.get(0).getReporttime().substring(8, 10) + "日" +
                    forecasts.get(0).getReporttime().substring(11, 13) + "时" + forecasts.get(0).getReporttime().substring(14, 16) + "分";
            //拼接天气预报消息
            String forecastWeatherMsg =
                    "您当前所在的城市为" + forecasts.get(0).getCity() + "。天气预报更新时间为" + time + "。下面是详细的天气情况:" +
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

            return sendWeatherMsg(robotWebHookAddress, forecastWeatherMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //发送天气预报失败代码
        Result<Object> failResult = new Result<>();
        failResult.setFlag(false);
        failResult.setMessage("send forecast weather msg fail");
        failResult.setCode(StatusCode.ERROR);
        return failResult;
    }

    /**
     * 发送天气消息
     *
     * @param sendWeatherMsgApi 天气机器人hook链接
     * @param weatherMsg        组装好的天气消息
     * @return 返回的发送情况json
     */
    private Result sendWeatherMsg(String sendWeatherMsgApi, String weatherMsg) {

        //创建请求体map信息，携带AppId与AppSecret
        Map<String, Object> sendWeatherMsgRequestBody = new HashMap();
        sendWeatherMsgRequestBody.put("msg_type", "text");
        Map<String, String> requestBodyContext = new HashMap();
        requestBodyContext.put("text", weatherMsg);
        sendWeatherMsgRequestBody.put("content", requestBodyContext);

        try {
            // 将 Map 转换为 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(sendWeatherMsgRequestBody);
            //发送POST请求
            JSONObject sendWeatherMsgResponseJson =
                    HttpClient.doPostJson(sendWeatherMsgApi, json);
            //返回结果
            Result<Object> sendWeatherMsgResult = new Result<>();
            sendWeatherMsgResult.setData(sendWeatherMsgResponseJson);
            return sendWeatherMsgResult;

        }catch (Exception e){
            e.printStackTrace();
        }
        //发送消息失败代码
        Result<Object> failResult = new Result<>();
        failResult.setFlag(false);
        failResult.setMessage("send weather msg fail");
        failResult.setCode(StatusCode.ERROR);
        return failResult;
    }

    /**
     * 获取TenantAccessToken
     * @param getTenantAccessTokenAddress 获取TenantAccessTokenAddress链接地址
     * @param robotAppId
     * @param robotAppSecret
     * @return data
     */
    @Override
    public Result getTenantAccessToken(String getTenantAccessTokenAddress, String robotAppId, String robotAppSecret) {
        //创建请求体map信息，携带AppId与AppSecret
        Map<String, String> sendPostRequestBodyMap = new HashMap();
        sendPostRequestBodyMap.put("app_id", robotAppId);
        sendPostRequestBodyMap.put("app_secret", robotAppSecret);
        try {
            // 将 Map 转换为 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(sendPostRequestBodyMap);
            //用Httpclient工具类发送POST请求
            JSONObject getTenantAccessTokenResultResponseJson =
                    HttpClient.doPostJson(getTenantAccessTokenAddress, json);

            if(getTenantAccessTokenResultResponseJson.isEmpty()){
                //发送消息失败代码
                Result<Object> failResult = new Result<>();
                failResult.setFlag(false);
                failResult.setMessage("get tenantAccessToken fail, return response is null");
                failResult.setCode(StatusCode.ERROR);
                return failResult;
            }
            //把返回的json字符串转换为map
            Map<String, Object> getTenantAccessTokenResultResponseMap =
                    objectMapper.readValue(getTenantAccessTokenResultResponseJson.toString(), new TypeReference<Map<String, Object>>() {});
            //把map存入到返回结果
            Result<Object> returnGetTenantAccessTokenResult = new Result<>();
            returnGetTenantAccessTokenResult.setData(getTenantAccessTokenResultResponseMap);
            return returnGetTenantAccessTokenResult;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //发送消息失败代码
        Result<Object> failResult = new Result<>();
        failResult.setFlag(false);
        failResult.setMessage("get tenantAccessToken fail");
        failResult.setCode(StatusCode.ERROR);
        return failResult;
    }
}
