package com.fly.robot.dao;

import com.fly.robot.entity.TableFlybookToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableFlyTokenRepository extends JpaRepository<TableFlybookToken, Long> {
    //根据appId与appSecret获取最新的一条token数据
    TableFlybookToken findTopByAppIdAndAppSecretOrderByCreateAtDesc(String appId, String appSecret);

}
