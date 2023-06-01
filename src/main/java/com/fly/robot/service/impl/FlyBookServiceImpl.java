package com.fly.robot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.dao.TableFlyTokenRepository;
import com.fly.robot.pojo.*;
import com.fly.robot.entity.Result;
import com.fly.robot.service.FlyBookService;
import com.fly.robot.util.FastJSONObjectToDto;
import com.fly.robot.util.HttpClient;
import com.fly.robot.util.WeatherDtoToMsg;
import com.lark.oapi.Client;
import com.lark.oapi.service.im.v1.model.CreateMessageReq;
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody;
import com.lark.oapi.service.im.v1.model.CreateMessageResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlyBookServiceImpl implements FlyBookService {
    @Autowired
    private TableFlyTokenRepository tableFlyTokenRepository;

    /**
     * 发送天气消息到群聊中
     * @param robotWebHookAddress 群聊机器人发送消息 webhook地址
     * @param flyBookWeatherCode  天气类型代码
     * @param weatherDTO          天气数据
     * @return
     * @throws Exception
     */
    public String sendDefaultCityWeatherMsgToGroupChat(String robotWebHookAddress, String flyBookWeatherCode, WeatherDTO weatherDTO) throws Exception {
        Map<String, Object> sendWeatherMsgRequestBody = new HashMap();
        ObjectMapper mapToStringMapper = new ObjectMapper();
        Map<String, String> requestBodyContext = new HashMap();

        requestBodyContext.put("text", WeatherDtoToMsg.conversionWeatherDtoToMsg(weatherDTO, flyBookWeatherCode));
        sendWeatherMsgRequestBody.put("content", requestBodyContext);
        sendWeatherMsgRequestBody.put("msg_type", "text");

        return HttpClient.doPostJson(robotWebHookAddress, mapToStringMapper.writeValueAsString(sendWeatherMsgRequestBody)).toJSONString();
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
    public TableFlybookToken getToken(String getTokenAddress, String robotAppId, String robotAppSecret, String tokenType) throws Exception {

        List<TableFlybookToken> tableFlyBookTokens =
                tableFlyTokenRepository
                        .findTopByAppIdAndAppSecretOrderByCreateAtDesc(robotAppId, robotAppSecret);
        LocalDateTime nowTime = LocalDateTime.now();

        if (tableFlyBookTokens == null || tableFlyBookTokens.isEmpty())
            return doGetReqGetToken(getTokenAddress, robotAppId, robotAppSecret, tokenType);

        LocalDateTime expireAgo = nowTime.minus(tableFlyBookTokens.get(0).getTokenExpire() - 15 * 60, ChronoUnit.SECONDS);
        if (tableFlyBookTokens.get(0).getCreateAt().isBefore(expireAgo))
            return doGetReqGetToken(getTokenAddress, robotAppId, robotAppSecret, tokenType);

        return tableFlyBookTokens.get(0);
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
    private TableFlybookToken doGetReqGetToken(String getTokenAddress, String robotAppId,
                                               String robotAppSecret, String tokenType) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> sendPostRequestBodyMap = new HashMap();
        sendPostRequestBodyMap.put("app_id", robotAppId);
        sendPostRequestBodyMap.put("app_secret", robotAppSecret);
        JSONObject getTenantAccessTokenResJson =
                HttpClient.doPostJson(getTokenAddress,
                        objectMapper.writeValueAsString(sendPostRequestBodyMap));

        GetTenantAccessTokenResDTO getTenantAccessTokenRes =
                FastJSONObjectToDto.conversion(getTenantAccessTokenResJson, GetTenantAccessTokenResDTO.class);
        //TODO 需要加密与解密敏感数据
        TableFlybookToken tableFlybookToken = new TableFlybookToken();
        tableFlybookToken.setToken(getTenantAccessTokenRes.getTenantAccessToken());
        tableFlybookToken.setTokenExpire(getTenantAccessTokenRes.getExpire());
        tableFlybookToken.setTokenType(tokenType);
        tableFlybookToken.setAppId(robotAppId);
        tableFlybookToken.setAppSecret(robotAppSecret);
        tableFlybookToken.setCreateAt(LocalDateTime.now());
        tableFlyTokenRepository.save(tableFlybookToken);

        return tableFlybookToken;
    }

    /**
     * 发送查询后的天气消息到查询人
     *
     * @param robotAppId         发送消息的机器人AppId
     * @param robotAppSecret     发送消息的机器人AppSecret
     * @param openId             消息接收人的openId
     * @param flyBookWeatherCode 发送消息类型代码
     * @param weatherDto         天气数据dto
     * @return
     * @throws Exception
     */
    @Override
    public Result sendWeatherMsgToOpenId(String robotAppId,
                                         String robotAppSecret,
                                         String openId,
                                         String flyBookWeatherCode,
                                         String sendMsgType,
                                         WeatherDTO weatherDto) throws Exception {
        ObjectMapper mapToStringMapper = new ObjectMapper();
        HashMap<String, String> messageContentMap = new HashMap<>();
        CreateMessageReqBody createMessageReqBody = new CreateMessageReqBody();

        Client feishuClient = Client.newBuilder(robotAppId, robotAppSecret).build();
        createMessageReqBody.setReceiveId(openId);
        createMessageReqBody.setMsgType(sendMsgType);
        messageContentMap.put(sendMsgType, WeatherDtoToMsg.conversionWeatherDtoToMsg(weatherDto, flyBookWeatherCode));
        createMessageReqBody.setContent(mapToStringMapper.writeValueAsString(messageContentMap));

        CreateMessageResp resp = feishuClient.im().message()
                .create(CreateMessageReq.newBuilder()
                        .receiveIdType("open_id")
                        .createMessageReqBody(createMessageReqBody)
                        .build());

        if (!resp.success()) {
            System.out.printf("code:%s,msg:%s,reqId:%s%n"
                    , resp.getCode(), resp.getMsg(), resp.getRequestId());
        }
        return null;
    }
}
