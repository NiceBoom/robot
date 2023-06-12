package com.fly.robot.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.robot.dto.GetFlyBookMsgReceiveDTO;
import com.fly.robot.dto.WeatherDTO;
import com.fly.robot.entity.*;
import com.fly.robot.pojo.*;
import com.fly.robot.service.FlyBookService;
import com.fly.robot.service.WeatherService;
import com.fly.robot.util.MsgVerification;
import com.fly.robot.util.WeatherDtoToMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/flyBook")
@EnableScheduling
@Api(tags = "FlyBookController")
@Component
@Slf4j
public class FlyBookController {
    @Autowired
    private FlyBookService flyBookService;
    @Autowired
    private WeatherService weatherService;
    private final static Logger LOGGER =LoggerFactory.getLogger(FlyBookController.class);


    //发送默认城市实时天气数据消息
    //每天早八点35，每隔一小时发一次，一直到晚上21.30 发送实时天气数据
    //或者也可以手动发送
    @ApiOperation("发送实时天气消息")
    @Scheduled(cron = "0 35 8,9,10,11,12,13,14,15,16,17,18,19,20,21 * * ? ")
    @PostMapping("/sendLiveWeatherMsg")
    String sendLiveWeatherMsg() throws Exception {
        WeatherDTO defaultCityForecastWeatherDto = weatherService.getWeather(GaodeCode.BEIJING_CITY_ADCODE, GaodeCode.GET_LIVE_WEATHER_CODE);
        LOGGER.info("logger获取默认城市天气dto:{}", defaultCityForecastWeatherDto);
        String weatherMsg = WeatherDtoToMsg.conversionWeatherDtoToMsg(defaultCityForecastWeatherDto, FlyBookCode.SEND_LIVE_WEATHER_MSG_CODE);
        return flyBookService
                .sendDefaultCityWeatherMsgToGroupChat(FlyBookCode.SEND_LIVE_WEATHER_MSG_CODE, FlyBookCode.SEND_MSG_TEXT_TYPE, weatherMsg);
    }

    //发送默认城市未来天气预报情况
    //每天9:00、10:00、12:00、13:00、19:00、20:00自动发送未来的天气预报
    //或者也可以手动发送
    @Scheduled(cron = "0 10 9,10,12,13,19,20 * * ? ")
    @PostMapping("/sendForecastWeatherMsg")
    String sendForecastWeatherMsg() throws Exception {
        WeatherDTO defaultCityForecastWeatherDto = weatherService.getWeather(GaodeCode.BEIJING_CITY_ADCODE, GaodeCode.GET_FORECAST_WEATHER_CODE);
        String weatherMsg = WeatherDtoToMsg.conversionWeatherDtoToMsg(defaultCityForecastWeatherDto, FlyBookCode.SEND_FORECAST_WEATHER_MSG_CODE);
        return flyBookService
                .sendDefaultCityWeatherMsgToGroupChat(FlyBookCode.SEND_FORECAST_WEATHER_MSG_CODE, FlyBookCode.SEND_MSG_TEXT_TYPE, weatherMsg);
    }

    /**
     * 接收查询人的消息并返回相应的天气消息
     *
     * @param flyBookMsgReceiveString
     * @return
     * @throws Exception
     */
    @PostMapping("/feishuRobotReceiveMessageProcess")
    void feishuRobotReceiveMessageProcess(@RequestBody String flyBookMsgReceiveString) throws Exception {
        //TODO 校验推送类型
        System.out.println("飞书推送的消息: " + flyBookMsgReceiveString);

        ObjectMapper objectMapper = new ObjectMapper();
        GetFlyBookMsgReceiveDTO flyBookMsgReceiveDTO = objectMapper.readValue(flyBookMsgReceiveString, GetFlyBookMsgReceiveDTO.class);

        String openId = flyBookMsgReceiveDTO.getEvent().getSender().getSender_id().getOpen_id();
        String msgContent = flyBookMsgReceiveDTO.getEvent().getMessage().getContent();
        Map<String, String> contentMap = objectMapper.readValue(msgContent, new TypeReference<Map<String, String>>() {
        });
        //查询实时天气
        if (FlyBookCode.SEARCH_LIVE_WEATHER_MSG_CODE
                .equals(MsgVerification
                        .queryWeatherMsgVerification(contentMap.get("text")))) {
            AddressAdcode addressAdcode = weatherService.findAddressInfoByMsg(contentMap.get("text").split(",|，")[0]);
            WeatherDTO getLiveWeatherDto = weatherService.getWeather(addressAdcode.getAdcode(), GaodeCode.GET_LIVE_WEATHER_CODE);
            String sendLiveWeatherMsg = WeatherDtoToMsg.conversionWeatherDtoToMsg(getLiveWeatherDto, FlyBookCode.SEARCH_LIVE_WEATHER_MSG_CODE);
            flyBookService.sendWeatherMsgToOpenId(FlyBookCode.MSG_RECEIVE_ID_TYPE_OPEN_ID, openId, FlyBookCode.SEND_MSG_TEXT_TYPE, sendLiveWeatherMsg);
            return;
        }
        //查询未来天气
        if (FlyBookCode.SEARCH_FORECAST_WEATHER_MSG_CODE
                .equals(MsgVerification
                        .queryWeatherMsgVerification(contentMap.get("text")))) {
            AddressAdcode addressAdcode = weatherService.findAddressInfoByMsg(contentMap.get("text").split(",|，")[0]);
            WeatherDTO getForecastWeatherDto = weatherService.getWeather(addressAdcode.getAdcode(), GaodeCode.GET_FORECAST_WEATHER_CODE);
            String sendForecastWeatherMsg = WeatherDtoToMsg.conversionWeatherDtoToMsg(getForecastWeatherDto, FlyBookCode.SEARCH_FORECAST_WEATHER_MSG_CODE);
            flyBookService.sendWeatherMsgToOpenId(FlyBookCode.MSG_RECEIVE_ID_TYPE_OPEN_ID, openId, FlyBookCode.SEND_MSG_TEXT_TYPE, sendForecastWeatherMsg);
            return;
        }
        //发送的消息格式不符合，发送错误提示消息
        flyBookService.sendWeatherMsgToOpenId(FlyBookCode.MSG_RECEIVE_ID_TYPE_OPEN_ID, openId, FlyBookCode.SEND_MSG_TEXT_TYPE, FlyBookCode.SEND_ERROR_WEATHER_MSG);

    }
}
