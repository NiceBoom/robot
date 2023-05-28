package com.fly.robot.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.entity.FlyBookConfig;
import com.fly.robot.entity.GaodeConfig;
import com.fly.robot.entity.Result;
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
    String feishuRobotReceiveMessageProcess(@RequestBody String requestBody) {
        try {
            //处理请求体变为map
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> requestBodyMap = objectMapper.readValue(requestBody, Map.class);
            System.out.println("requestBodyMap is: " + requestBodyMap);
            //校验请求格式
            //url_verification 代表这是一个验证请求
            if ("url_verification".equals(requestBodyMap.get("type"))) {
                //获取其中的challenge值并返回
                //未设置Encrypt_key 即原样返回值
                //TODO 设置加密方法
                HashMap<String, String> verificationUrlResultString = new HashMap<>();
                verificationUrlResultString.put("challenge", (String) requestBodyMap.get("challenge"));
                System.out.println(verificationUrlResultString.toString());
                String jsonString = objectMapper.writeValueAsString(verificationUrlResultString);
                System.out.println(jsonString);
                return jsonString;
            }
            //校验推送类型
            //获取查询人的openid以及需要查询的天气的城市
            //TODO 后续需要优化消息类型判断
            //获取查询人的openId
            Map<String, Object> event = (Map<String, Object>) requestBodyMap.get("event");
            System.out.println("event is: " + event.toString());
            Map<String, Object> sender = (Map<String, Object>) event.get("sender");
            System.out.println("sender is: " + sender.toString());
            Map<String, Object> sender_id = (Map<String, Object>) sender.get("sender_id");
            String openId = (String) sender_id.get("open_id");
            System.out.println("openId is: " + openId);
            //获取查询人的具体消息
            Map<String, Object> message = (Map<String, Object>) event.get("message");
            String contentString = (String) message.get("content");
            try {
                Map<String, Object> contentMap = objectMapper.readValue(contentString, new TypeReference<Map<String, Object>>() {});
                // 使用转换后的Map对象进行操作,获取具体消息
                String msgText = (String) contentMap.get("text");
                //调用高德api获取城市adcode
                Result addressInfoByMsg = weatherService.findAddressInfoByMsg(GaodeConfig.GET_ADDRESS_ADCODE_URL, webApiKey, msgText);
                System.out.println("address info  is: " + addressInfoByMsg.toString());
                //提取出该地址的adcode代码
                JSONObject addressInfoJson = (JSONObject) addressInfoByMsg.getData();
                List<Map<String, Object>> geocodes = (List<Map<String, Object>>)addressInfoJson.get("geocodes");
                String adcode = (String)geocodes.get(0).get("adcode");
                //获取该地址未来天气预报
                Result forecastWeather = weatherService.findForecastWeather(webApiKey, adcode, adcode);
                System.out.println("forecastWeather is: " + forecastWeather);
                //发送改地址未来天气预报并@这个人
                flyBookService.sendForecastWeatherMsgToOpenId(FlyBookConfig.SEND_MSG_TO_USER_URL,tenantAccessToken, openId, forecastWeather.getData().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
