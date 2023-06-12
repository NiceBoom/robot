package com.fly.robot.dao;

import com.fly.robot.entity.FlybookToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlyTokenRepository extends JpaRepository<FlybookToken, Long> {
    //根据appId与appSecret获取最新的一条token数据
    FlybookToken findTopByAppIdAndAppSecretOrderByCreateAtDesc(String appId, String appSecret);

}
