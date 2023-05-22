package com.fly.robot.controller;

import com.fly.robot.service.FlyBookService;
import org.springframework.beans.factory.annotation.Autowired;
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

    //发送实时天气数据消息
    //每天早八点半，每隔一小时发一次，一直到晚上21.30 发送实时天气数据
    //或者也可以手动发送
    @Scheduled(cron = "0 30 8,9,10,11,12,13,14,15,16,17,18,19,20,21 * * ? *")
    @PostMapping("/sendLiveWeatherMsg")
    String sendLiveWeatherMsg(){
        //发送实时天气数据消息
        String resultJSON = flyBookService.sendLiveWeatherMsg();
        return resultJSON;
    }

    //发送未来天气预报情况
    //每天9:00、10:00、12:00、13:00、19:00、20:00自动发送未来的天气预报
    //或者也可以手动发送
    @Scheduled(cron = "0 0 9,10,12,13,19,20 * * ? *")
    @PostMapping("/sendForecastWeatherMsg")
    String sendForecastWeatherMsg(){

        String resultJSON = flyBookService.sendForecastWeatherMsg();
        return resultJSON;
    }
}
