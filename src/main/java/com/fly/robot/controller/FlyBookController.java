package com.fly.robot.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.entity.FlyBookConfig;
import com.fly.robot.entity.GaodeConfig;
import com.fly.robot.entity.Result;
import com.fly.robot.pojo.*;
import com.fly.robot.service.FlyBookService;
import com.fly.robot.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

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

    //发送实时天气数据消息
    //每天早八点35，每隔一小时发一次，一直到晚上21.30 发送实时天气数据
    //或者也可以手动发送
    @Scheduled(cron = "0 35 8,9,10,11,12,13,14,15,16,17,18,19,20,21 * * ? ")
    @PostMapping("/sendLiveWeatherMsg")
    String sendLiveWeatherMsg() throws Exception {
        return flyBookService
                .sendDefaultCityWeatherMsgToGroupChat(robotWebHookAddress,
                        FlyBookConfig.SEND_LIVE_WEATHER_MSG_CODE,
                        weatherService.getWeather(webApiKey, GaodeConfig.BEIJING_CITY_ADCODE, GaodeConfig.GET_LIVE_WEATHER_CODE));
    }

    //发送未来天气预报情况
    //每天9:00、10:00、12:00、13:00、19:00、20:00自动发送未来的天气预报
    //或者也可以手动发送
    @Scheduled(cron = "0 10 9,10,12,13,19,20 * * ? ")
    @PostMapping("/sendForecastWeatherMsg")
    String sendForecastWeatherMsg() throws Exception {
        return flyBookService
                .sendDefaultCityWeatherMsgToGroupChat(robotWebHookAddress,
                        FlyBookConfig.SEND_FORECAST_WEATHER_MSG_CODE,
                        weatherService.getWeather(webApiKey, GaodeConfig.BEIJING_CITY_ADCODE, GaodeConfig.GET_FORECAST_WEATHER_CODE));
    }

    //获取Tenant Access Token
    //Tenant Access Token 代表使用应用的身份操作 OpenAPI，API 所能操作的数据资源范围受限于应用的身份所能操作的资源范围
    //如果你的业务逻辑不需要操作用户的数据资源，仅需操作应用自己拥有的资源（比如在应用自己的文档目录空间下创建云文档），则推荐使用 Tenant Access Token，无需额外申请授权。
    @GetMapping("/getTenantAccessToken")
    TableFlybookToken getTenantAccessToken() throws Exception {
        return flyBookService.getToken(FlyBookConfig.GET_TENANT_ACCESS_TOKEN_ADDRESS, robotAppId, robotAppSecret, FlyBookConfig.GET_FLYBOOK_TENANT_ACCESS_TOKEN);
    }

    /**
     * 接收查询人的消息并返回相应的天气消息
     *
     * @param flyBookMsgReceiveString
     * @return
     * @throws Exception
     */
    @PostMapping("/feishuRobotReceiveMessageProcess")
    String feishuRobotReceiveMessageProcess(@RequestBody String flyBookMsgReceiveString) throws Exception {
        //TODO 校验推送类型
        System.out.println("飞书推送的消息: " + flyBookMsgReceiveString);

        ObjectMapper objectMapper = new ObjectMapper();
        GetFlyBookMsgReceiveDTO flyBookMsgReceiveDTO = objectMapper.readValue(flyBookMsgReceiveString, GetFlyBookMsgReceiveDTO.class);

        String openId = flyBookMsgReceiveDTO.getEvent().getSender().getSender_id().getOpen_id();
        String msgContent = flyBookMsgReceiveDTO.getEvent().getMessage().getContent();
        Map<String, String> contentMap = objectMapper.readValue(msgContent, new TypeReference<Map<String, String>>() {
        });
        String msgText = contentMap.get("text");
        TableAddressAdcode tableAddressAdcode = weatherService.findAddressInfoByMsg(GaodeConfig.GET_ADDRESS_ADCODE_URL, webApiKey, msgText);

        if (msgText.contains("实时") || msgText.contains("现在") || msgText.contains("目前") ||
                msgText.contains("当前") || msgText.contains("当下") || msgText.contains("眼下") || msgText.contains("现状") ||
                msgText.contains("本时") || msgText.contains("即时")) {

            WeatherDTO getLiveWeatherDto = weatherService.getWeather(webApiKey, tableAddressAdcode.getAdcode(), GaodeConfig.GET_LIVE_WEATHER_CODE);
            Result result = flyBookService.
                    sendWeatherMsgToOpenId(robotAppId, robotAppSecret, openId, FlyBookConfig.SEND_LIVE_WEATHER_MSG_CODE, FlyBookConfig.SEND_MSG_TEXT_TYPE, getLiveWeatherDto);
            return result.toString();
        } else {
            WeatherDTO getForecastWeatherDto = weatherService.getWeather(webApiKey, tableAddressAdcode.getAdcode(), GaodeConfig.GET_FORECAST_WEATHER_CODE);
            Result result = flyBookService.
                    sendWeatherMsgToOpenId(robotAppId, robotAppSecret, openId, openId, FlyBookConfig.SEND_MSG_TEXT_TYPE, getForecastWeatherDto);
            return result.toString();
        }
    }
}
