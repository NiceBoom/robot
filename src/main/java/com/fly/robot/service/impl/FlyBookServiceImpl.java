package com.fly.robot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.dao.TableFlyTokenRepository;
import com.fly.robot.pojo.*;
import com.fly.robot.dao.TableForecastWeatherRepository;
import com.fly.robot.dao.TableLiveWeatherRepository;
import com.fly.robot.entity.Result;
import com.fly.robot.entity.StatusCode;
import com.fly.robot.service.FlyBookService;
import com.fly.robot.util.FastJSONObjectToDto;
import com.fly.robot.util.HttpClient;
import com.fly.robot.util.WeatherDtoToMsg;
import com.lark.oapi.Client;
import com.lark.oapi.service.im.v1.model.CreateMessageReq;
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody;
import com.lark.oapi.service.im.v1.model.CreateMessageResp;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlyBookServiceImpl implements FlyBookService {

    @Autowired
    private TableLiveWeatherRepository tableLiveWeatherRepository;

    @Autowired
    private TableForecastWeatherRepository tableForecastWeatherRepository;

    @Autowired
    private TableFlyTokenRepository tableFlyTokenRepository;

    @Value("${feishu.appid}")
    private String robotAppId; //飞书机器人appId

    @Value("${feishu.app-secret}")
    private String robotAppSecret; //飞书机器人秘钥

    /**
     * 发送实时天气数据消息
     *
     * @param robotWebHookAddress 消息机器人web hook地址
     * @param cityId              城市代码
     * @return
     */
    @Override
    public Result sendLiveWeatherMsg(String robotWebHookAddress, String cityId) {
        try {
            //从mysql获取最新一条实时天气数据
            List<TableLiveWeather> liveWeatherFromMysql = tableLiveWeatherRepository.findFirstByCityIdOrderByCreateAtDesc(cityId);
            String liveWeather = liveWeatherFromMysql.get(0).getLiveWeather();

            //格式化实时天气数据，转换成DTO
            ObjectMapper mapper = new ObjectMapper();
            LiveWeatherDTO liveWeatherDto = mapper.readValue(liveWeather, LiveWeatherDTO.class);
            //使用工具类转换dto为天气消息
            String liveWeatherMsg = WeatherDtoToMsg.conversionLiveWeatherDtoToMsg(liveWeatherDto);

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
    public Result sendForecastWeatherMsg(String robotWebHookAddress, String cityId) {
        try {
            //从mysql查找最新的一条天气预报数据
            List<TableForecastWeather> forecastWeatherFromMysql = tableForecastWeatherRepository.findFirstByCityIdOrderByCreateAtDesc(cityId);
            //获取天气预报信息
            String forecastWeather = forecastWeatherFromMysql.get(0).getForecastWeather();
            //格式化天气预报数据，转换成DTO
            ObjectMapper mapper = new ObjectMapper();
            ForecastWeatherDTO forecastWeatherDto = mapper.readValue(forecastWeather, ForecastWeatherDTO.class);
            //使用dto转换msg工具类
            String forecastWeatherMsg = WeatherDtoToMsg.conversionForecastWeatherDtoToMsg(forecastWeatherDto);

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

        } catch (Exception e) {
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
     *
     * @param getTokenAddress 获取Token链接地址
     * @param robotAppId
     * @param robotAppSecret
     * @param tokenType       获取的token类型
     * @return data
     */
    @Override
    public Result getToken(String getTokenAddress, String robotAppId, String robotAppSecret, String tokenType) {
        //从数据库获取tenantAccessToken
        List<TableFlybookToken> token =
                tableFlyTokenRepository.findTopByAppIdAndAppSecretOrderByCreateAtDesc(robotAppId, robotAppSecret);
        //获取当前时间
        LocalDateTime nowTime = LocalDateTime.now();
        //如果没有token，则获取一个新的token保存到mysql并返回数据
        if (token == null || token.isEmpty()) {
            return doGetReqGetToken(getTokenAddress, robotAppId, robotAppSecret, tokenType);
        }
        //获取该token过期时间，有效期缩短15分钟，
        LocalDateTime expireAgo = nowTime.minus(token.get(0).getTokenExpire() - 15 * 60, ChronoUnit.SECONDS);
        //如果token已过期，则获取一个新的token保存到mysql并返回数据
        if (token.get(0).getCreateAt().isBefore(expireAgo)) {
            return doGetReqGetToken(getTokenAddress, robotAppId, robotAppSecret, tokenType);
        }
        //如果token没过期，则直接返回
        Result<Object> returnGetTenantAccessTokenResult = new Result<>();
        returnGetTenantAccessTokenResult.setData(token.get(0));
        return returnGetTenantAccessTokenResult;
    }

    /**
     * 发送get请求获取token并存到mysql中
     *
     * @param getTokenAddress 获取Token链接地址
     * @param robotAppId
     * @param robotAppSecret
     * @param tokenType       获取的token类型
     * @return data
     */
    public Result doGetReqGetToken(String getTokenAddress, String robotAppId, String robotAppSecret, String tokenType) {
        try {
            //创建请求体map信息，携带AppId与AppSecret
            Map<String, String> sendPostRequestBodyMap = new HashMap();
            sendPostRequestBodyMap.put("app_id", robotAppId);
            sendPostRequestBodyMap.put("app_secret", robotAppSecret);
            // 将 Map 转换为 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(sendPostRequestBodyMap);
            //用Httpclient工具类发送POST请求
            JSONObject getTenantAccessTokenResultResponseJson =
                    HttpClient.doPostJson(getTokenAddress, json);
            if (getTenantAccessTokenResultResponseJson.isEmpty()) {
                //发送消息失败代码
                Result<Object> failResult = new Result<>();
                failResult.setFlag(false);
                failResult.setMessage("get tenantAccessToken fail");
                failResult.setCode(StatusCode.ERROR);
                return failResult;
            }
            //把返回的json字符串转换为dto
            GetTenantAccessTokenResDTO conversion =
                    FastJSONObjectToDto.conversion(getTenantAccessTokenResultResponseJson, GetTenantAccessTokenResDTO.class);
            //组装存入mysql中的数据
            //TODO 需要加密与解密敏感数据
            TableFlybookToken tableFlybookToken = new TableFlybookToken();
            tableFlybookToken.setToken(conversion.getTenantAccessToken());
            tableFlybookToken.setTokenExpire(conversion.getExpire());
            tableFlybookToken.setTokenType(tokenType);
            tableFlybookToken.setAppId(robotAppId);
            tableFlybookToken.setAppSecret(robotAppSecret);
            tableFlybookToken.setCreateAt(LocalDateTime.now());
            tableFlyTokenRepository.save(tableFlybookToken);

            //把token存入到返回结果
            Result<Object> returnGetTenantAccessTokenResult = new Result<>();
            returnGetTenantAccessTokenResult.setData(tableFlybookToken);
            return returnGetTenantAccessTokenResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //发送消息失败代码
        Result<Object> failResult = new Result<>();
        failResult.setFlag(false);
        failResult.setMessage("get tenantAccessToken fail");
        failResult.setCode(StatusCode.ERROR);
        return failResult;
    }

    /**
     * 发送查询地址的未来天气预报并@查询人
     *
     * @param sendMsgUrl         发送消息地址
     * @param tenantAccessToken
     * @param openId             查询人的OpenId
     * @param forecastWeatherDto 未来的天气预报信息
     * @return
     */
    @Override
    public Result sendForecastWeatherMsgToOpenId(String sendMsgUrl,
                                                 String tenantAccessToken,
                                                 String openId,
                                                 ForecastWeatherDTO forecastWeatherDto) throws Exception {
        //创建飞书API Client
        Client feishuClient = Client.newBuilder(robotAppId, robotAppSecret).build();
        //创建请求体
        CreateMessageReqBody createMessageReqBody = new CreateMessageReqBody();
        createMessageReqBody.setReceiveId(openId);
        //TODO 优化消息类型 https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/message/create 到pojo里
        createMessageReqBody.setMsgType("text");
        System.out.println("组装好的请求体： " + createMessageReqBody.toString());
        //创建消息文本
        HashMap<String, String> messageContentMap = new HashMap<>();
        messageContentMap.put("text", WeatherDtoToMsg.conversionSearchForecastWeatherDtoToMsg(forecastWeatherDto));
        //转换为json字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(messageContentMap);
        System.out.println(jsonString.toString());
        createMessageReqBody.setContent(jsonString);
        //发起请求
        CreateMessageResp resp = feishuClient.im().message()
                .create(CreateMessageReq.newBuilder()
                        .receiveIdType("open_id")
                        .createMessageReqBody(createMessageReqBody)
                        .build());
        System.out.println("响应消息" + resp.toString());

        // 处理服务端错误
        if (!resp.success()) {
            System.out.println(String.format("code:%s,msg:%s,reqId:%s"
                    , resp.getCode(), resp.getMsg(), resp.getRequestId()));
            Result failResult = new Result();
            failResult.setCode(StatusCode.ERROR);
            failResult.setMessage(String.format("code:%s,msg:%s,reqId:%s"
                    , resp.getCode(), resp.getMsg(), resp.getRequestId()));
            failResult.setData(resp.toString());
            return failResult;
        }
        Result successResult = new Result();
        successResult.setData(resp);
        return successResult;
    }
}
