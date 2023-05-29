package com.fly.robot.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.entity.FlyBookConfig;
import com.fly.robot.entity.GaodeConfig;
import com.fly.robot.entity.Result;
import com.fly.robot.pojo.ForecastWeatherDTO;
import com.fly.robot.pojo.GetAddressInfoFromGaodeDTO;
import com.fly.robot.pojo.GetFlyBookMsgReceiveDTO;
import com.fly.robot.service.FlyBookService;
import com.fly.robot.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/flyBook")
@Component
@EnableScheduling
public class FlyBookController {
    @Autowired
    private FlyBookService flyBookService;

    @Autowired
    private WeatherService weatherService;

    @Value("${feishu.robot-webhook-address}")
    private String robotWebHookAddress;//读取飞书robot web hook 地址

    @Value("${feishu.appid}")
    private String robotAppId; //飞书机器人appId

    @Value("${feishu.app-secret}")
    private String robotAppSecret; //飞书机器人秘钥

    @Value("${gaode.web-api-key}")
    private String webApiKey; //读取高德ApiKey

    @Value("${feishu.tenant-access-token}")
    private String tenantAccessToken; //读取测试用高德tenant_access_token
    //TODO 用完删除，从redis获取tenantAccessToken

    //发送实时天气数据消息
    //每天早八点35，每隔一小时发一次，一直到晚上21.30 发送实时天气数据
    //或者也可以手动发送
//    @Scheduled(cron = "0/5 * * * * ? ")
    @Scheduled(cron = "0 35 8,9,10,11,12,13,14,15,16,17,18,19,20,21 * * ? ")
    @PostMapping("/sendLiveWeatherMsg")
    Result sendLiveWeatherMsg() {
        //发送实时天气数据消息
        return flyBookService.sendLiveWeatherMsg(robotWebHookAddress);
    }

    //发送未来天气预报情况
    //每天9:00、10:00、12:00、13:00、19:00、20:00自动发送未来的天气预报
    //或者也可以手动发送
    @Scheduled(cron = "0 10 9,10,12,13,19,20 * * ? ")
    @PostMapping("/sendForecastWeatherMsg")
    Result sendForecastWeatherMsg() {
        return flyBookService.sendForecastWeatherMsg(robotWebHookAddress);
    }

    //获取Tenant Access Token
    //Tenant Access Token 代表使用应用的身份操作 OpenAPI，API 所能操作的数据资源范围受限于应用的身份所能操作的资源范围
    //如果你的业务逻辑不需要操作用户的数据资源，仅需操作应用自己拥有的资源（比如在应用自己的文档目录空间下创建云文档），则推荐使用 Tenant Access Token，无需额外申请授权。
    @GetMapping("/getTenantAccessToken")
    Result getTenantAccessToken() {
        return flyBookService.getTenantAccessToken(FlyBookConfig.GET_TENANT_ACCESS_TOKEN_ADDRESS, robotAppId, robotAppSecret);
    }

    //处理飞书推送消息
    @PostMapping("/feishuRobotReceiveMessageProcess")
    String feishuRobotReceiveMessageProcess(@RequestBody String flyBookMsgReceiveString) {
        //这段为验证地址代码
//            //处理请求体变为map
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, Object> requestBodyMap = objectMapper.readValue(requestBody, Map.class);
//            System.out.println("requestBodyMap is: " + requestBodyMap);
//            //校验请求格式
//            //url_verification 代表这是一个验证请求
//            if ("url_verification".equals(requestBodyMap.get("type"))) {
//                //获取其中的challenge值并返回
//                //未设置Encrypt_key 即原样返回值
//                //TODO 设置加密方法
//                HashMap<String, String> verificationUrlResultString = new HashMap<>();
//                verificationUrlResultString.put("challenge", (String) requestBodyMap.get("challenge"));
//                System.out.println(verificationUrlResultString.toString());
//                String jsonString = objectMapper.writeValueAsString(verificationUrlResultString);
//                System.out.println(jsonString);
//                return jsonString;

        //校验推送类型
        //获取查询人的openid以及需要查询的天气的城市
        //TODO 后续需要优化消息类型判断
        System.out.println("飞书推送的消息"+flyBookMsgReceiveString);
        //把字符串转换为dto
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            GetFlyBookMsgReceiveDTO flyBookMsgReceiveDTO = objectMapper.readValue(flyBookMsgReceiveString, GetFlyBookMsgReceiveDTO.class);

        //获取查询人的openId
        String openId = flyBookMsgReceiveDTO.getEvent().getSender().getSender_id().getOpen_id();
        System.out.println("查询人的openid" + openId);
        //获取查询人的具体消息
        //消息格式为 消息类型：消息内容，为string的json字符串
        String msgContent = flyBookMsgReceiveDTO.getEvent().getMessage().getContent();
        System.out.println("查询人的消息内容" + msgContent);
        //获取群聊ID
        String chatId = flyBookMsgReceiveDTO.getEvent().getMessage().getChat_id();
            Map<String, Object> contentMap = objectMapper.readValue(msgContent, new TypeReference<Map<String, Object>>() {
            });
            // 使用转换后的Map对象进行操作,获取具体消息
            String msgText = (String) contentMap.get("text");
            System.out.println("查询人的具体消息" + msgText);
            //调用高德api获取城市adcode
            Result addressInfoByMsg = weatherService.findAddressInfoByMsg(GaodeConfig.GET_ADDRESS_ADCODE_URL, webApiKey, msgText);
            System.out.println("address info  is: " + addressInfoByMsg.toString());
            //提取出该地址的adcode代码
            GetAddressInfoFromGaodeDTO addressInfoJson = (GetAddressInfoFromGaodeDTO) addressInfoByMsg.getData();
            String adcode = addressInfoJson.getGeocodes().get(0).getAdcode();
            System.out.println("address adcode is: " + adcode);
            //获取该地址未来天气预报
            Result forecastWeather = weatherService.findForecastWeather(webApiKey, adcode, GaodeConfig.GET_FORECAST_WEATHER_CODE);
            System.out.println("forecastWeather is: " + forecastWeather);
            ForecastWeatherDTO forecastWeatherDto = (ForecastWeatherDTO) forecastWeather.getData();
            //发送改地址未来天气预报并@这个人
            Result result = flyBookService.
                    sendForecastWeatherMsgToOpenId(FlyBookConfig.SEND_MSG_TO_USER_URL, tenantAccessToken, openId, chatId, forecastWeatherDto);
            System.out.println(result.getData().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
