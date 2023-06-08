package com.fly.robot;

import com.fly.robot.entity.TableFlybookToken;
import com.fly.robot.pojo.FlyBookConfig;
import com.fly.robot.service.FlyBookService;
import com.fly.robot.service.RedisService;
import com.fly.robot.service.WeatherService;
import com.fly.robot.service.impl.FlyBookServiceImpl;
import com.fly.robot.util.WeatherDtoToMsg;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RobotApplicationTests {

    @Test
    void getToken() throws Exception{
    }

}
