package com.fly.robot.controller;

import com.fly.robot.entity.Result;
import com.fly.robot.service.FlyBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/flyBook")
public class FlyBookController {
    @Autowired
    private FlyBookService flyBookService;

    @Value("${feishu.robot-webhook-address}")
    private String robotWebHookAddress;//读取飞书robot web hook 地址

    //发送实时天气数据消息
    //每天早八点35，每隔一小时发一次，一直到晚上21.30 发送实时天气数据
    //或者也可以手动发送
    @Scheduled(cron = "0 35 8,9,10,11,12,13,14,15,16,17,18,19,20,21 * * ? *")
    @PostMapping("/sendLiveWeatherMsg")
    Result sendLiveWeatherMsg(){
        //发送实时天气数据消息
        return flyBookService.sendLiveWeatherMsg(robotWebHookAddress);
    }

    //发送未来天气预报情况
    //每天9:00、10:00、12:00、13:00、19:00、20:00自动发送未来的天气预报
    //或者也可以手动发送
    @Scheduled(cron = "0 10 9,10,12,13,19,20 * * ? *")
    @PostMapping("/sendForecastWeatherMsg")
    Result sendForecastWeatherMsg(){
        return flyBookService.sendForecastWeatherMsg(robotWebHookAddress);
    }
}
