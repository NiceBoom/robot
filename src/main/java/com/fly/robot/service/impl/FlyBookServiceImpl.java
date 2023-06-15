package com.fly.robot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fly.robot.controller.FlyBookController;
import com.fly.robot.dao.FlyTokenRepository;
import com.fly.robot.dto.GetTenantAccessTokenResDTO;
import com.fly.robot.entity.FlybookToken;
import com.fly.robot.pojo.FlyBookCode;
import com.fly.robot.service.FlyBookService;
import com.fly.robot.service.RedisService;
import com.fly.robot.util.FastJSONObjectToDto;
import com.fly.robot.util.HttpClient;
import com.lark.oapi.Client;
import com.lark.oapi.service.im.v1.model.CreateMessageReq;
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody;
import com.lark.oapi.service.im.v1.model.CreateMessageResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
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
    private FlyTokenRepository flyTokenRepository;
    @Autowired
    private RedisService redisService;

    private final static Logger LOGGER = LoggerFactory.getLogger(FlyBookServiceImpl.class);

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
    public FlybookToken getToken(String tokenType) throws Exception {

        LocalDateTime nowTime = LocalDateTime.now();
        ObjectMapper objectMapper = new ObjectMapper();
        String redisToken = redisService.get(robotAppId + robotAppSecret);

        if (redisToken == null) {
            FlybookToken flybookToken = flyTokenRepository
                    .findTopByAppIdAndAppSecretOrderByCreateAtDesc(robotAppId, robotAppSecret);
            if (flybookToken == null)
                return doGetReqGetToken(tokenType);
            LocalDateTime expireAgo = nowTime.minus(flybookToken.getTokenExpire() - 10 * 60, ChronoUnit.SECONDS);
            if (flybookToken.getCreateAt().isBefore(expireAgo))
                return doGetReqGetToken(tokenType);
            return flybookToken;
        }
        return objectMapper.readValue(redisToken, FlybookToken.class);
    }

    /**
     * 发送get请求获取token并存到redis与mysql中
     *
     * @param tokenType 获取的token类型
     * @return data
     */
    private FlybookToken doGetReqGetToken(String tokenType) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Map<String, String> sendPostRequestBodyMap = new HashMap();
        sendPostRequestBodyMap.put("app_id", robotAppId);
        sendPostRequestBodyMap.put("app_secret", robotAppSecret);
        JSONObject getTenantAccessTokenResJson =
                HttpClient.doPostJson(FlyBookCode.GET_TENANT_ACCESS_TOKEN_ADDRESS,
                        objectMapper.writeValueAsString(sendPostRequestBodyMap));

        GetTenantAccessTokenResDTO getTenantAccessTokenRes =
                FastJSONObjectToDto.conversion(getTenantAccessTokenResJson, GetTenantAccessTokenResDTO.class);
        //TODO 需要加密与解密敏感数据
        FlybookToken flybookToken = new FlybookToken();
        flybookToken.setToken(getTenantAccessTokenRes.getTenantAccessToken());
        flybookToken.setTokenExpire(getTenantAccessTokenRes.getExpire());
        flybookToken.setTokenType(tokenType);
        flybookToken.setAppId(robotAppId);
        flybookToken.setAppSecret(robotAppSecret);
        flybookToken.setCreateAt(LocalDateTime.now());
        flyTokenRepository.save(flybookToken);

        redisService.set(robotAppId + robotAppSecret, objectMapper.writeValueAsString(flybookToken));
        redisService.expire(robotAppId + robotAppSecret, flybookToken.getTokenExpire() - 10 * 60);

        return flybookToken;
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
    public void sendWeatherMsgToOpenId(String idType,
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
            LOGGER.info("code:%s,msg:%s,reqId:%s%n"
                    , resp.getCode(), resp.getMsg(), resp.getRequestId());
        }
    }
}
