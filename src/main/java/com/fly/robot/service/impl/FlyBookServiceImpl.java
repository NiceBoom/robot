package com.fly.robot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.dao.TableForecastWeatherRepository;
import com.fly.robot.dao.TableLiveWeatherRepository;
import com.fly.robot.entity.Result;
import com.fly.robot.entity.StatusCode;
import com.fly.robot.pojo.*;
import com.fly.robot.service.FlyBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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
        try {
            //得到connection对象
            URL httpUrl = new URL(sendWeatherMsgApi);
            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
            //设置请求方式
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/json");

            // 设置请求体
            String requestBody = "{\n" +
                    "    \"msg_type\": \"text\",\n" +
                    "    \"content\": {\n" +
                    "        \"text\": \"" + weatherMsg + "\"\n" +
                    "    }\n" +
                    "}";
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(requestBody);
            writer.close();
            //连接
            connection.connect();
            // 获取状态码 响应结果
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                reader.close();
                System.out.println(buffer.toString());
                // 断开连接
                connection.disconnect();

                Result<Object> sendWeatherMsgResult = new Result<>();
                sendWeatherMsgResult.setData(buffer.toString());

                return sendWeatherMsgResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //发送消息失败代码
        Result<Object> failResult = new Result<>();
        failResult.setFlag(false);
        failResult.setMessage("send forecast weather msg fail");
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

        try {
            //得到connection对象
            URL httpUrl = new URL(getTenantAccessTokenAddress);
            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
            //设置请求方式
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // 设置请求体
            String requestBody = "{\n" +
                    "    \"app_id\": \"" + robotAppId + "\",\n" +
                    "    \"app_secret\": \"" + robotAppSecret + "\",\n" +
                    "}";
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(requestBody);
            writer.close();
            //连接
            connection.connect();
            // 获取状态码 响应结果
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                reader.close();
                System.out.println(buffer.toString());
                // 断开连接
                connection.disconnect();

                Result<Object> sendWeatherMsgResult = new Result<>();
                //处理响应数据 格式化回报响应
                ObjectMapper mapper = new ObjectMapper();
                GetTenantAccessTokenResponseDto getTenantAccessTokenResponseDto =
                        mapper.readValue(buffer.toString(), GetTenantAccessTokenResponseDto.class);

                //TODO 处理返回的错误代码  待优化
                //错误代码地址 https://open.feishu.cn/document/ukTMukTMukTM/ugjM14COyUjL4ITN  通用错误码
                sendWeatherMsgResult.setData(getTenantAccessTokenResponseDto.getTenantAccessToken());

                return sendWeatherMsgResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //发送消息失败代码
        Result<Object> failResult = new Result<>();
        failResult.setFlag(false);
        failResult.setMessage("send forecast weather msg fail");
        failResult.setCode(StatusCode.ERROR);
        return failResult;
    }
}
