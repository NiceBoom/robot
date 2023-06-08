package com.fly.robot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fly.robot.dao.TableFlyTokenRepository;
import com.fly.robot.dto.GetTenantAccessTokenResDTO;
import com.fly.robot.entity.TableFlybookToken;
import com.fly.robot.dto.WeatherDTO;
import com.fly.robot.pojo.FlyBookConfig;
import com.fly.robot.pojo.Result;
import com.fly.robot.service.FlyBookService;
import com.fly.robot.service.RedisService;
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

import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlyBookServiceImpl implements FlyBookService {
    @Value("${feishu.robot-webhook-address}")
    private String robotWebHookAddress;//读取飞书robot web hook 地址
    @Value("${feishu.appid}")
    private String robotAppId; //飞书机器人appId
    @Value("${feishu.app-secret}")
    private String robotAppSecret; //飞书机器人秘钥
    @Autowired
    private TableFlyTokenRepository tableFlyTokenRepository;
    @Autowired
    private RedisService redisService;

    /**
     * 发送默认城市天气到群聊
     *
     * @param flyBookWeatherCode 天气类型代码
     * @param sendMsgType
     * @param msg                消息内容
     * @return
     * @throws Exception
     */
    public String sendDefaultCityWeatherMsgToGroupChat(String flyBookWeatherCode, String sendMsgType, String msg) throws Exception {
        Map<String, Object> sendWeatherMsgRequestBody = new HashMap();
        ObjectMapper mapToStringMapper = new ObjectMapper();
        Map<String, String> requestBodyContext = new HashMap();

        requestBodyContext.put("text", msg);
        sendWeatherMsgRequestBody.put("content", requestBodyContext);
        sendWeatherMsgRequestBody.put("msg_type", sendMsgType);

        return HttpClient.doPostJson(robotWebHookAddress, mapToStringMapper.writeValueAsString(sendWeatherMsgRequestBody)).toJSONString();
    }

    /**
     * 获取TenantAccessToken
     *
     * @param tokenType 获取的token类型
     * @return data
     */
    @Override
    public TableFlybookToken getToken(String tokenType) throws Exception {

        LocalDateTime nowTime = LocalDateTime.now();
        ObjectMapper objectMapper = new ObjectMapper();
        String redisToken = redisService.get(robotAppId + robotAppSecret);

        if (redisToken == null) {
            TableFlybookToken tableFlybookToken = tableFlyTokenRepository
                    .findTopByAppIdAndAppSecretOrderByCreateAtDesc(robotAppId, robotAppSecret);

            if (tableFlybookToken == null)
                return doGetReqGetToken(FlyBookConfig.GET_TENANT_ACCESS_TOKEN_ADDRESS, robotAppId, robotAppSecret, tokenType);
            LocalDateTime expireAgo = nowTime.minus(tableFlybookToken.getTokenExpire() - 10 * 60, ChronoUnit.SECONDS);
            if (tableFlybookToken.getCreateAt().isBefore(expireAgo))
                return doGetReqGetToken(FlyBookConfig.GET_TENANT_ACCESS_TOKEN_ADDRESS, robotAppId, robotAppSecret, tokenType);
            return tableFlybookToken;
        }
        return objectMapper.readValue(redisToken, TableFlybookToken.class);
    }

    /**
     * 发送get请求获取token并存到redis与mysql中
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
        objectMapper.registerModule(new JavaTimeModule());
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

        redisService.set(robotAppId + robotAppSecret, objectMapper.writeValueAsString(tableFlybookToken));
        redisService.expire(robotAppId + robotAppSecret, tableFlybookToken.getTokenExpire() - 10 * 60);

        return tableFlybookToken;
    }

    /**
     * 发送查询后的天气消息到查询人
     *
     * @param id          消息接收人的openId
     * @param sendMsgType 消息类型
     * @param msg         消息内容
     * @return
     * @throws Exception
     */
    @Override
    public Result sendWeatherMsgToOpenId(String idType,
                                         String id,
                                         String sendMsgType,
                                         String msg) throws Exception {
        ObjectMapper mapToStringMapper = new ObjectMapper();
        HashMap<String, String> messageContentMap = new HashMap<>();
        CreateMessageReqBody createMessageReqBody = new CreateMessageReqBody();

        Client feishuClient = Client.newBuilder(robotAppId, robotAppSecret).build();
        createMessageReqBody.setReceiveId(id);
        createMessageReqBody.setMsgType(sendMsgType);
        messageContentMap.put(sendMsgType, msg);
        createMessageReqBody.setContent(mapToStringMapper.writeValueAsString(messageContentMap));

        CreateMessageResp resp = feishuClient.im().message()
                .create(CreateMessageReq.newBuilder()
                        .receiveIdType(idType)
                        .createMessageReqBody(createMessageReqBody)
                        .build());

        if (!resp.success()) {
            System.out.printf("code:%s,msg:%s,reqId:%s%n"
                    , resp.getCode(), resp.getMsg(), resp.getRequestId());
        }
        return null;
    }
}
